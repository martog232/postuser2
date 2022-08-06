package com.example.postuser.services.post;

import com.example.postuser.controllers.error.APIErrorCode;
import com.example.postuser.exceptions.EntityNotFoundException;
import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.dto.user.UserWithNameDTO;
import com.example.postuser.model.dto.user.UserWithoutPassDTO;
import com.example.postuser.model.entities.Image;
import com.example.postuser.model.entities.Post;
import com.example.postuser.model.entities.User;
import com.example.postuser.model.repositories.PostRepository;
import com.example.postuser.model.repositories.UserRepository;
import com.example.postuser.services.image.ImageService;
import com.example.postuser.services.user.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private static final String ASSETS_DIR = new File("").getAbsolutePath() + File.separator + "assets";
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private ImageService imageService;

    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<PostDTO> findById(Integer postId) {
        return Optional.ofNullable(postRepository.findById(postId).map(this::mapToDTO).orElseThrow(() ->
                new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription())));
    }

    public PostDTO create(String content, List<MultipartFile> photoList, Integer loggedUser) throws IOException {
        PostDTO postDTO = new PostDTO();
        postDTO.setContent(content);
        postDTO.setImageList(new ArrayList<>());
        postDTO.setLikers(new ArrayList<>());
        postDTO.setComments(new ArrayList<>());

        UserWithNameDTO loggedUserDto = modelMapper.
                map(userService.findById(loggedUser).orElse(null), UserWithNameDTO.class);

        postDTO.setOwner(loggedUserDto);
        Post post = mapToEntity(postDTO);
        post = postRepository.save(post);
        for (MultipartFile multipartFile : photoList) {
            File pFile = new File(ASSETS_DIR + File.separator + post.getId() + "_" + System.nanoTime() + ".png");
            OutputStream os = new FileOutputStream(pFile);
            os.write(multipartFile.getBytes());
            Image image = new Image();
            image.setUrl(pFile.getAbsolutePath());
            image.setPost(mapToEntity(findById(post.getId()).get()));
            imageService.saveImage(image);
            os.close();

        }
        post = postRepository.save(post);
        return mapToDTO(post);
    }

    @Transactional
    public PostDTO likeAndUnlike(Integer postId, Integer loggedUserId) {

//TODO opravi like or make it void (returned post is without the new liker)

        Optional<PostDTO> postDTO = findById(postId);
        if (postDTO.isPresent()) {
            User u = userRepository.findById(loggedUserId).orElse(null);
            assert u != null;
            if (u.getLikedPosts().contains(postDTO.get())) {
                u.getLikedPosts().remove(mapToEntity(postDTO.get()));

            } else {
                u.getLikedPosts().add(mapToEntity(postDTO.get()));
                userRepository.save(u);
            }
        } else throw new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription());
//        return new PostDTO(postRepository.findById(postId).map(this::mapToDTO));
        return null;
    }

    public ResponseEntity<?> deletePost(Integer id, Integer loggedUser){
        PostDTO p = findById(id).orElseThrow(() -> new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription()));
        if(!Objects.equals(p.getOwner().getId(), loggedUser)){
            return new ResponseEntity<>("You cannot delete other's posts",HttpStatus.FORBIDDEN);
        }
        imageService.deleteByPostId(id);
        postRepository.deletePostById(p.getId());
        return new ResponseEntity<>("Post is deleted",HttpStatus.OK);
    }

    public Post mapToEntity(PostDTO postDTO) {
        return modelMapper.map(postDTO, Post.class);
    }

    public PostDTO mapToDTO(Post post) {
        return modelMapper.map(post, PostDTO.class);
    }

}