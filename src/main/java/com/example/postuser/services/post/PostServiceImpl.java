package com.example.postuser.services.post;

import com.example.postuser.controllers.error.APIErrorCode;
import com.example.postuser.exceptions.EntityNotFoundException;
import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.dto.post.PostWithoutOwnerDTO;
import com.example.postuser.model.dto.user.UserWithNameDTO;
import com.example.postuser.model.entities.Post;
import com.example.postuser.model.entities.User;
import com.example.postuser.model.repositories.PostRepository;
import com.example.postuser.model.repositories.UserRepository;
import com.example.postuser.services.user.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final UserRepository userRepository;

    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<PostDTO> findById(Integer postId) {
        return Optional.ofNullable(postRepository.findById(postId).map(this::mapToDTO).orElseThrow(() ->
                new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription())));
    }

    public PostDTO create(PostDTO postDTO, Integer loggedUser) {
        postDTO.setComments(new ArrayList<>());
        postDTO.setLikers(new ArrayList<>());
        UserWithNameDTO loggedUserDto = modelMapper.
                map(userService.findById(loggedUser).get(), UserWithNameDTO.class);
        postDTO.setOwner(loggedUserDto);
        Post post = mapToEntity(postDTO);
        post = postRepository.save(post);
        return mapToDTO(post);
    }

    @Transactional
    public PostWithoutOwnerDTO like(Integer postId, Integer loggedUserId) {

//TODO opravi like

        Optional<PostDTO> postDTO = findById(postId);
        if (postDTO.isPresent()) {
            User u = userRepository.findById(loggedUserId).get();
            if (u.getLikedPosts().contains(postDTO.get())) {
                u.getLikedPosts().remove(mapToEntity(postDTO.get()));

            } else {
                u.getLikedPosts().add(mapToEntity(postDTO.get()));
                userRepository.save(u);
            }
        } else throw new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription());
        return new PostWithoutOwnerDTO(postRepository.findById(postId).map(this::mapToDTO).get());
    }

    public Post mapToEntity(PostDTO postDTO) {
        return modelMapper.map(postDTO, Post.class);
    }

    public PostDTO mapToDTO(Post post) {
        return modelMapper.map(post, PostDTO.class);
    }

}