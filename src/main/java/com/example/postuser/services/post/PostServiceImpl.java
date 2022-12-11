package com.example.postuser.services.post;

import com.example.postuser.controllers.error.APIErrorCode;
import com.example.postuser.exceptions.EntityNotFoundException;
import com.example.postuser.model.dto.comment.CommentDTO;
import com.example.postuser.model.dto.group.GroupDTO;
import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.dto.user.UserWithNameDTO;
import com.example.postuser.model.dto.user.UserWithoutPassDTO;
import com.example.postuser.model.entities.*;
import com.example.postuser.model.repositories.PostRepository;
import com.example.postuser.model.repositories.UserRepository;
import com.example.postuser.services.comment.CommentService;
import com.example.postuser.services.group.GroupService;
import com.example.postuser.services.image.ImageService;
import com.example.postuser.services.user.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.*;
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
    private final ImageService imageService;
    private final CommentService commentService;

    private final GroupService groupService;

    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<PostDTO> getAllFollowingsPostsInMainPage(Integer loggedUserId) {
        List<UserWithNameDTO> followings = userService.getAllFollowings(loggedUserId);
        List<PostDTO> allFollowingPostDtos = new LinkedList<>();
        for (UserWithNameDTO following : followings) {
            List<PostDTO> followingsPosts = getAllPostsByOwnerIdAndNotInGroup(following.getId());
            allFollowingPostDtos.addAll(followingsPosts);
        }
        return allFollowingPostDtos;
    }

    public List<PostDTO> getAllPostsByOwnerIdAndNotInGroup(Integer ownerId) {
        List<Post> ownerPosts = postRepository.getAllByOwnerIdAndNotInGroup(ownerId);
        List<PostDTO> ownerPostDtos = new LinkedList<>();
        for (Post post : ownerPosts) {
            ownerPostDtos.add(mapToDTO(post));
        }
        return ownerPostDtos;
    }

    public ResponseEntity<?> getAllPostsByGroup(Integer groupId, Integer loggedUserId) {
        UserWithoutPassDTO user = userService.getUserWithoutPassDTOById(loggedUserId).get();
        List<GroupDTO> joinedGroups = user.getGroupMember();
        for (GroupDTO joinedGroup : joinedGroups) {
            if (joinedGroup.getId().equals(groupId)) {
                return new ResponseEntity<>(postRepository.getAllByGroup(groupId).stream().map(this::mapToDTO).collect(Collectors.toList()), HttpStatus.OK);
            }

        }
        return new ResponseEntity<>("you are not member in this group" + "! First join it", HttpStatus.METHOD_NOT_ALLOWED);
    }


    @Override
    public ResponseEntity<?> findById(Integer postId, int loggedUser) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            PostDTO postDTO = mapToDTO(post.get());
            UserWithNameDTO userDTO = userService.getUserWithNameDTOById(loggedUser).get();
            if (postDTO.getGroup() != null) {
                List<UserWithNameDTO> members = postDTO.getGroup().getMembers();
                for (UserWithNameDTO member : members) {
                    if (member.getId().equals(userDTO.getId())) {
                        return new ResponseEntity<>(postDTO, HttpStatus.OK);
                    }
                    return new ResponseEntity<>("you are not member in group " + postDTO.getGroup().getName() +
                            " ! First join it", HttpStatus.METHOD_NOT_ALLOWED);
                }
            }
            return new ResponseEntity<>(postDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public Optional<PostDTO> findById(Integer postId) {
        return Optional.ofNullable(postRepository.findById(postId).map(this::mapToDTO).orElseThrow(() ->
                new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription())));
    }

    @Override
    public ResponseEntity<?> create(String content, List<MultipartFile> photoList, Integer loggedUser, Integer groupId) throws IOException {
        PostDTO postDTO = new PostDTO();
        postDTO.setContent(content);
        postDTO.setImageList(new ArrayList<>());
        postDTO.setLikers(new ArrayList<>());
        postDTO.setComments(new ArrayList<>());
        postDTO.setCreated(LocalDateTime.now());

        UserWithNameDTO loggedUserDto = userService.getUserWithNameDTOById(loggedUser).get();
        postDTO.setOwner(loggedUserDto);
        boolean isMember = false;

        if (groupId != null) {
            Optional<GroupDTO> group = groupService.findById(groupId);

            if (group.isPresent()) {
                List<UserWithNameDTO> members = group.get().getMembers();
                for (UserWithNameDTO member : members) {
                    if (member.getId().equals(loggedUserDto.getId())) {
                        isMember = true;
                        break;
                    }
                }
                if (isMember) {
                    postDTO.setGroup(group.get());
                } else {
                    return new ResponseEntity<>("you are not member in group " + group.get().getName() + " ! First join it", HttpStatus.METHOD_NOT_ALLOWED);
                }
            } else {
                return new ResponseEntity<>("Group not found", HttpStatus.NOT_FOUND);
            }
        }
        Post post = mapToEntity(postDTO);
        post = postRepository.save(post);
        if(photoList!=null){
        if (photoList.size()>0) {
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
        }}
        post = postRepository.save(post);
        return new ResponseEntity<>(mapToDTO(mapToEntity(findById(post.getId()).get())), HttpStatus.OK);
    }


    public ResponseEntity<?> likeAndUnlike(Integer postId, Integer loggedUserId) {

        Optional<PostDTO> postDTO = findById(postId);
        if (postDTO.isPresent()) {
            User u = userRepository.findById(loggedUserId).orElse(null);
            assert u != null;
            boolean isMember = false;
            if (postDTO.get().getGroup() != null) {
                List<UserWithNameDTO> members = postDTO.get().getGroup().getMembers();
                for (UserWithNameDTO member : members) {
                    if (member.getId().equals(u.getId())) {
                        isMember = true;
                        break;
                    }
                }
                if (!isMember)
                    return new ResponseEntity<>("you are not member in group " + postDTO.get().getGroup().getName() +
                            " ! First join it", HttpStatus.METHOD_NOT_ALLOWED);
            }
            List<Post> likedPosts = u.getLikedPosts();

            if (u.getLikedPosts().contains(mapToEntity(postDTO.get()))) {
                u.getLikedPosts().remove(mapToEntity(postDTO.get()));
            } else {
                u.getLikedPosts().add(mapToEntity(postDTO.get()));
            }
            u.setLikedPosts(likedPosts);
            userRepository.saveAndFlush(u);

        } else throw new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription());
        return new ResponseEntity<>(findById(postId),HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deletePost(Integer id, Integer loggedUserId) {
        PostDTO p = findById(id).orElseThrow(() -> new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription()));
        UserWithoutPassDTO loggedUserDto = userService.getUserWithoutPassDTOById(loggedUserId).get();

        if (p.getGroup() != null) {
            List<GroupDTO> loggedUserGroups = loggedUserDto.getGroupAdmin();
            for (GroupDTO groupDTO : loggedUserGroups) {
                if (groupDTO.getId().equals(p.getGroup().getId())) {

                    commentService.deleteAllByPost(id);
                    imageService.deleteByPostId(id);
                    postRepository.deletePostById(p.getId());
                    return new ResponseEntity<>("Post is deleted", HttpStatus.OK);
                }
            }
        }
        if (!Objects.equals(p.getOwner().getId(), loggedUserId)) {
            return new ResponseEntity<>("You cannot delete other's posts", HttpStatus.FORBIDDEN);
        }

        commentService.deleteAllByPost(id);
        imageService.deleteByPostId(id);
        postRepository.deletePostById(p.getId());
        return new ResponseEntity<>("Post is deleted", HttpStatus.OK);
    }

    public ResponseEntity<?> addComment(Integer postId, String commentContent, Integer loggedUser) {
        PostDTO postDTO = findById(postId).orElseThrow(() -> new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription()));

        CommentDTO createdComment = commentService.createComment(commentContent, postDTO, loggedUser);

        List<CommentDTO> postComments = postDTO.getComments();
        postComments.add(createdComment);
        postDTO.setComments(postComments);
        postRepository.saveAndFlush(mapToEntity(postDTO));

        return new ResponseEntity<>(findById(postId).get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> likeAndUnlikeComment(Integer id, int loggedUser) {
        Optional<CommentDTO> optionalCommentDTO = commentService.findById(id);
        if (optionalCommentDTO.isPresent()) {
            User u = userRepository.findById(loggedUser).orElse(null);
            assert u != null;
            CommentDTO commentDTO = optionalCommentDTO.get();
            GroupDTO groupDTO = commentDTO.getPost().getGroup();
            boolean isMember = false;
            if (groupDTO != null) {
                List<UserWithNameDTO> members = groupDTO.getMembers();
                for (UserWithNameDTO member : members) {
                    if (member.getId().equals(u.getId())) {
                        isMember = true;
                        break;
                    }
                }
                if (!isMember) return new ResponseEntity<>("you are not member in group " + groupDTO.getName() +
                        " ! First join it", HttpStatus.METHOD_NOT_ALLOWED);
            }
            List<Comment> likedComments = u.getLikedComments();

            if (likedComments.contains(commentService.mapToEntity(commentDTO))) {
                likedComments.remove(commentService.mapToEntity(commentDTO));
            } else {
                likedComments.add(commentService.mapToEntity(commentDTO));
            }
            u.setLikedComments(likedComments);
            userRepository.saveAndFlush(u);

        } else throw new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription());

        return new ResponseEntity<>(commentService.findById(id).get(), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> editPost(Integer postId, String content, int loggedUserId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            if (post.getOwner().getId().equals(loggedUserId)) {
                if (post.getGroup() != null) {
                    boolean isMember = false;
                    List<User> groupMembers = post.getGroup().getMembers();
                    for (User u : groupMembers) {
                        if (u.getId().equals(loggedUserId)) {
                            isMember = true;
                            break;
                        }
                    }
                    if (!isMember)
                        return new ResponseEntity<>("you are not member in group " + post.getGroup().getName() +
                                " ! First join it", HttpStatus.METHOD_NOT_ALLOWED);
                }
                post.setContent(content);
                postRepository.save(post);
                return new ResponseEntity<>(mapToDTO(post), HttpStatus.OK);
            }
            return new ResponseEntity<>("You cannot edit other user's posts", HttpStatus.FORBIDDEN);

        } else throw new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription());
    }

    public Post mapToEntity(PostDTO postDTO) {
        return modelMapper.map(postDTO, Post.class);
    }

    public PostDTO mapToDTO(Post post) {
        return modelMapper.map(post, PostDTO.class);
    }
}
