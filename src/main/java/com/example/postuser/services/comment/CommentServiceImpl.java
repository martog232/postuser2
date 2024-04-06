package com.example.postuser.services.comment;

import com.example.postuser.controllers.error.APIErrorCode;
import com.example.postuser.exceptions.EntityNotFoundException;
import com.example.postuser.model.dto.comment.CommentDTO;
import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.entities.Comment;
import com.example.postuser.model.entities.User;
import com.example.postuser.model.repositories.CommentRepository;
import com.example.postuser.services.user.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final ModelMapper modelMapper;

    private final UserService userService;

    private final CommentRepository commentRepository;

    public CommentDTO mapToDTO(Comment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }

    public Comment mapToEntity(CommentDTO commentDTO) {
        return modelMapper.map(commentDTO, Comment.class);
    }

    @Transactional
    public ResponseEntity<?> deleteComment(Integer commentId, Integer loggedUser) {
        CommentDTO p = findById(commentId).orElseThrow(() ->
                new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription()));
        if (!Objects.equals(p.getOwner().getId(), loggedUser)) {
            return new ResponseEntity<>("You cannot delete other's posts", HttpStatus.FORBIDDEN);
        }
        commentRepository.deleteCommentById(p.getId());

        return new ResponseEntity<>("Comment is deleted", HttpStatus.OK);
    }

    @Override
    public Optional<CommentDTO> findById(Integer commentId) {
        return Optional.ofNullable(commentRepository.findById(commentId).map(this::mapToDTO).orElseThrow(() ->
                new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription())));
    }

    @Override
    public Optional<Comment> getCommentById(Integer commentId) {
        return Optional.ofNullable(commentRepository.findById(commentId).orElseThrow(() ->
                new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription())));
    }

    @Override
    public CommentDTO createComment(String content, PostDTO postDTO, Integer loggedUser) {
        CommentDTO createdComment = new CommentDTO();
        createdComment.setOwner(userService.getUserWithNameDTOById(loggedUser).get());
        createdComment.setPost(postDTO);
        createdComment.setLikers(new LinkedList<>());
        createdComment.setContent(content);

        return createdComment;
    }


    @Override
    public void deleteAllByPost(Integer id) {
        commentRepository.deleteAllByPost(id);
    }

    @Override
    public ResponseEntity<?> editComment(Integer commentId, String content, int loggedUserId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            if (comment.getOwner().getId().equals(loggedUserId)) {
                if (comment.getPost().getGroup() != null) {
                    boolean isMember = false;
                    List<User> groupMembers = comment.getPost().getGroup().getMembers();
                    for (User u : groupMembers) {
                        if (u.getId().equals(loggedUserId)) {
                            isMember = true;
                            break;
                        }
                    }
                    if (!isMember)
                        return new ResponseEntity<>("you are not member in group " + comment.getPost().getGroup().getName() +
                                " ! First join it", HttpStatus.METHOD_NOT_ALLOWED);
                }
                comment.setContent(content);
                commentRepository.save(comment);
                return new ResponseEntity<>(mapToDTO(comment), HttpStatus.OK);
            }
            return new ResponseEntity<>("You cannot edit other user's posts", HttpStatus.FORBIDDEN);

        } else throw new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription());


    }

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

}
