package com.example.postuser.services.post;

import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.entities.Post;
import org.springframework.expression.AccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {

    List<PostDTO> getAllPosts();

    List<PostDTO> getAllFollowingsPostsInMainPage(Integer loggedUserId);

    ResponseEntity<?> findById(Integer postId, int loggedUser);

    ResponseEntity<?> getAllPostsByGroup(Integer groupId, Integer loggedUserId);

    ResponseEntity<?> create(String content, List<MultipartFile> photoList, Integer loggedUser, Integer groupId) throws IOException;

    ResponseEntity<?> likeAndUnlike(Integer postId, Integer loggedUserId);

    ResponseEntity<?> deletePost(Integer id, Integer loggedUser) throws AccessException;

    Post mapToEntity(PostDTO postDTO);

    PostDTO mapToDTO(Post post);

    ResponseEntity<?> addComment(Integer postId, String commentContent, Integer loggedUser);

    ResponseEntity<?> likeAndUnlikeComment(Integer id, int loggedUser);
}
