package com.example.postuser.services.comment;

import com.example.postuser.controllers.error.APIErrorCode;
import com.example.postuser.exceptions.EntityNotFoundException;
import com.example.postuser.model.dto.comment.CommentDTO;
import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.entities.Comment;
import com.example.postuser.model.repositories.CommentRepository;
import com.example.postuser.services.user.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


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

    public ResponseEntity<?> getAllByPostId(Integer postId) {
        return new ResponseEntity<>(commentRepository.getAllByPostId(postId).stream().map(this::mapToDTO).collect(Collectors.toList()), HttpStatus.OK);
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

}
