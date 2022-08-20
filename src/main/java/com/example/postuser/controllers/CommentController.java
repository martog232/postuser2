package com.example.postuser.controllers;

import com.example.postuser.controllers.config.ControllerConfig;
import com.example.postuser.model.dto.comment.CommentDTO;
import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.services.comment.CommentService;
import com.example.postuser.services.post.PostService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(ControllerConfig.COMMENTS_URL)
public class CommentController {


    private SessionManager sessionManager;

    private final CommentService commentService;
    private PostService postService;

    @DeleteMapping(value = "/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer commentId, HttpSession ses) throws AuthenticationException {
        return commentService.deleteComment(commentId, sessionManager.getLoggedUser(ses));
    }

    @GetMapping(value = "/{id}")
    public Optional<CommentDTO> findById(@PathVariable(name = "id") Integer commentId) {
        return commentService.findById(commentId);
    }
}
