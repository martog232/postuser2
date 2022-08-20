package com.example.postuser.controllers;

import com.example.postuser.controllers.config.ControllerConfig;
import com.example.postuser.model.dto.comment.CommentDTO;
import com.example.postuser.model.dto.post.PostDTO;
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
@RequestMapping(ControllerConfig.POSTS_URL)
public class PostController {

    private final PostService postService;
    private SessionManager sessionManager;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<PostDTO> getAll() {

        return postService.getAllPosts();
    }
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @RequestMapping("/main-page")
    public List<PostDTO> mainPage(HttpSession ses) throws AuthenticationException {

        return postService.getAllFollowingsPosts(sessionManager.getLoggedUser(ses));
    }

    @GetMapping(value = "/{post_id}")
    public Optional<PostDTO> findById(@PathVariable(name = "post_id") Integer postId) {
        return postService.findById(postId);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public PostDTO create(@RequestParam String content, @ModelAttribute List<MultipartFile> photoList, HttpSession ses)
            throws AuthenticationException, IOException {

        return postService.create(content, photoList, sessionManager.getLoggedUser(ses));
    }

    @PutMapping(value = "/{id}")
    public PostDTO likeAndUnlike(@PathVariable(name = "id") Integer id, HttpSession ses) throws Exception {

        return postService.likeAndUnlike(id, sessionManager.getLoggedUser(ses));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Integer id, HttpSession ses) throws AccessException {

        return postService.deletePost(id, (Integer) ses.getAttribute("LoggedUser"));
    }
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},value = "/add-comment/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public  ResponseEntity<?> addComment(@PathVariable Integer id, @RequestParam String commentContent, HttpSession ses) throws AuthenticationException {

        return postService.addComment(id, commentContent, sessionManager.getLoggedUser(ses));
    }

    //GetMapping paging
    //PutMapping editPost
    //PutMapping likePost
    //
}
