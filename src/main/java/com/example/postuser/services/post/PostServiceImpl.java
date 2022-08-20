package com.example.postuser.services.post;

import com.example.postuser.controllers.error.APIErrorCode;
import com.example.postuser.exceptions.EntityNotFoundException;
import com.example.postuser.model.dto.comment.CommentDTO;
import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.dto.user.UserWithNameDTO;
import com.example.postuser.model.entities.Image;
import com.example.postuser.model.entities.Post;
import com.example.postuser.model.entities.User;
import com.example.postuser.model.repositories.PostRepository;
import com.example.postuser.model.repositories.UserRepository;
import com.example.postuser.services.comment.CommentService;
import com.example.postuser.services.image.ImageService;
import com.example.postuser.services.user.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
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
    private CommentService commentService;

    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<PostDTO> getAllFollowingsPosts(Integer loggedUserId) {
        List<UserWithNameDTO> followings = userService.getAllFollowings(loggedUserId);
        List<PostDTO> allFollowingPostDtos = new LinkedList<>();
        for (UserWithNameDTO following : followings) {
            List<PostDTO> followingsPosts = getAllPostsByOwnerId(following.getId());
            allFollowingPostDtos.addAll(followingsPosts);
        }
        return allFollowingPostDtos;
    }

    public List<PostDTO> getAllPostsByOwnerId(Integer ownerId) {
        List<Post> ownerPosts = postRepository.getAllByOwnerId(ownerId);
        List<PostDTO> ownerPostDtos = new LinkedList<>();
        for (Post post : ownerPosts) {
            ownerPostDtos.add(mapToDTO(post));
        }
        return ownerPostDtos;
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
                map(userService.getUserWithoutPassDTOById(loggedUser).orElse(null), UserWithNameDTO.class);

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
        Optional<PostDTO> postDTO = findById(postId);
        if (postDTO.isPresent()) {
            User u = userRepository.findById(loggedUserId).orElse(null);
            assert u != null;
            List<Post> likedPosts = u.getLikedPosts();
            if (u.getLikedPosts().contains(mapToEntity(postDTO.get()))) {
                u.getLikedPosts().remove(mapToEntity(postDTO.get()));
            } else {
                u.getLikedPosts().add(mapToEntity(postDTO.get()));
            }
            u.setLikedPosts(likedPosts);
            userRepository.save(u);
        } else throw new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription());
        return mapToDTO(postRepository.findById(postId).get());

    }

    public ResponseEntity<?> deletePost(Integer id, Integer loggedUser) {
        PostDTO p = findById(id).orElseThrow(() -> new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription()));
        if (!Objects.equals(p.getOwner().getId(), loggedUser)) {
            return new ResponseEntity<>("You cannot delete other's posts", HttpStatus.FORBIDDEN);
        }
        imageService.deleteByPostId(id);
        postRepository.deletePostById(p.getId());
        return new ResponseEntity<>("Post is deleted", HttpStatus.OK);
    }

    public Post mapToEntity(PostDTO postDTO) {
        return modelMapper.map(postDTO, Post.class);
    }

    public PostDTO mapToDTO(Post post) {
        return modelMapper.map(post, PostDTO.class);
    }

    public ResponseEntity<?> addComment(Integer postId, String commentContent, Integer loggedUser) {
        PostDTO postDTO = findById(postId).orElseThrow(() -> new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription()));

            CommentDTO createdComment = commentService.createComment(commentContent,postDTO,loggedUser);

            List<CommentDTO> postComments = postDTO.getComments();
            postComments.add(createdComment);
            postDTO.setComments(postComments);
            postRepository.save(mapToEntity(postDTO));

            return new ResponseEntity<>(findById(postId).get(),HttpStatus.OK);
        }

    // TODO: 16.8.2022 г. comment add
    //                            delete
    //                            edit
    //                            like/unlike
}