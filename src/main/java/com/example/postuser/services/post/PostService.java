package com.example.postuser.services.post;

import com.example.postuser.model.dto.comment.CommentDTO;
import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.entities.Comment;
import com.example.postuser.model.entities.Post;
import org.springframework.expression.AccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PostService {

    List<PostDTO> getAllPosts();

    List<PostDTO> getAllFollowingsPosts(Integer loggedUserId);

    Optional<PostDTO> findById(Integer postId);

    PostDTO create(String content, List<MultipartFile> photoList, Integer loggedUser) throws IOException;

    PostDTO likeAndUnlike(Integer postId, Integer loggedUserId);

    ResponseEntity<?> deletePost(Integer id, Integer loggedUser) throws AccessException;

    Post mapToEntity(PostDTO postDTO);

    PostDTO mapToDTO(Post post);

    ResponseEntity<?> addComment(Integer postId, String commentContent, Integer loggedUser);
}
