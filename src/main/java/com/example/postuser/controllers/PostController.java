package com.example.postuser.controllers;

import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.dto.post.PostWithoutOwnerDTO;
import com.example.postuser.services.post.PostService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.expression.AccessException;
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
public class PostController {

    private final PostService postService;
    private SessionManager sessionManager;

    @GetMapping(value = "posts", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<PostDTO> getAll() {

        return postService.getAllPosts();
    }

    @GetMapping(value = "posts/{post_id}")
    public Optional<PostDTO> findById(@PathVariable(name = "post_id") Integer postId) {
        return postService.findById(postId);
    }

    @PostMapping(value = "posts",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    public PostDTO create(@RequestParam String content, @ModelAttribute List<MultipartFile> photoList, HttpSession ses) throws AuthenticationException, IOException {
        if (ses.getAttribute("LoggedUser") == null) {
            throw new AuthenticationException("first log in");
        }
        return postService.create(content, photoList, sessionManager.getLoggedUser(ses));
    }

    @PutMapping(value = "posts/{id}")
    public PostDTO likeAndUnlike(@PathVariable(name = "id") Integer id, HttpSession ses) throws Exception {
        if (ses.getAttribute("LoggedUser") == null) {
            throw new AuthenticationException("first log in");
        }
        return postService.likeAndUnlike(id, sessionManager.getLoggedUser(ses));
    }

    @DeleteMapping(value = "posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Integer id, HttpSession ses) throws AuthenticationException, AccessException {
        if (ses.getAttribute("LoggedUser") == null) {
            throw new AuthenticationException("first log in");
        }
       return postService.deletePost(id,(Integer) ses.getAttribute("LoggedUser"));
    }


    //GetMapping paging
    //PutMapping editPost
    //PutMapping likePost
    //
}
