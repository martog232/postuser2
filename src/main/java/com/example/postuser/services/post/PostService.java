package com.example.postuser.services.post;

import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.dto.post.PostWithoutOwnerDTO;
import com.example.postuser.model.entities.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    List<PostDTO> getAllPosts();

    Optional<PostDTO> findById(Integer postId);

    PostDTO create(PostDTO postDTO, Integer loggedUser);

    PostWithoutOwnerDTO likeAndUnlike(Integer postId, Integer loggedUserId);

    Post mapToEntity(PostDTO postDTO);

    PostDTO mapToDTO(Post post);
}
