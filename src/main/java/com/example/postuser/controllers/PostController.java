package com.example.postuser.controllers;

import com.example.postuser.controllers.config.ControllerConfig;
import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.services.group.GroupService;
import com.example.postuser.services.post.PostService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

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

        return postService.getAllFollowingsPostsInMainPage(sessionManager.getLoggedUser(ses));
    }

    @GetMapping(value = "/{post_id}")
    public ResponseEntity<?> findById(@PathVariable(name = "post_id") Integer postId, HttpSession ses) throws AuthenticationException {
        return postService.findById(postId, sessionManager.getLoggedUser(ses));
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestParam String content, @ModelAttribute List<MultipartFile> photoList, HttpSession ses,@Nullable @RequestParam(required = false) Integer groupId)
            throws AuthenticationException, IOException {
        return postService.create(content, photoList, sessionManager.getLoggedUser(ses), groupId);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> likeAndUnlike(@PathVariable(name = "id") Integer id, HttpSession ses) throws Exception {

        return postService.likeAndUnlike(id, sessionManager.getLoggedUser(ses));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Integer id, HttpSession ses) throws AccessException {

        return postService.deletePost(id, (Integer) ses.getAttribute("LoggedUser"));
    }


}
