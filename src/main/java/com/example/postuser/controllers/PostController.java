package com.example.postuser.controllers;

import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.dto.post.PostWithoutOwnerDTO;
import com.example.postuser.model.dto.user.UserWithNameDTO;
import com.example.postuser.model.repositories.UserRepository;
import com.example.postuser.services.PostService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor

public class PostController {

    private final PostService postService;

    @GetMapping(value = "posts", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<PostDTO> getAll() {

        return postService.getAllPosts();
    }

    @GetMapping(value = "posts/{post_id}")
    public Optional<PostDTO> findById(@PathVariable(name = "post_id") Integer postId) {
        return postService.findById(postId);
    }

    @PostMapping(value = "posts")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDTO create(@RequestBody PostDTO postDTO, HttpSession ses) throws AuthenticationException {
        if (ses.getAttribute("LoggedUser") == null) {
            throw new AuthenticationException("first log in");
        }
        return postService.create(postDTO, (Integer) ses.getAttribute("LoggedUser"));
    }

    @PutMapping(value = "posts/{id}")
    public PostWithoutOwnerDTO like(@PathVariable(name = "id") int id, HttpSession ses) throws Exception {
        if (ses.getAttribute("LoggedUser") == null) {
            throw new AuthenticationException("first log in");
        }
        return postService.like(id, (Integer) ses.getAttribute("LoggedUser"));
    }

    //GetMapping paging
    //PutMapping editPost
    //PutMapping likePost
    //
}
