package com.example.postuser.services.comment;

import com.example.postuser.model.dto.comment.CommentDTO;
import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.entities.Comment;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface CommentService {

    CommentDTO mapToDTO(Comment comment);

    Comment mapToEntity(CommentDTO commentDTO);

    ResponseEntity<?> deleteComment(Integer commentId, Integer loggedUser);

    Optional<CommentDTO> findById(Integer commentId);

    CommentDTO createComment(String content, PostDTO postDTO, Integer loggedUser);

    void deleteAllByPost(Integer id);
}
