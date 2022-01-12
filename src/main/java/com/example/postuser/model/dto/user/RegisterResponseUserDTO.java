package com.example.postuser.model.dto.user;

import com.example.postuser.model.entities.Comment;
import com.example.postuser.model.entities.Post;
import com.example.postuser.model.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RegisterResponseUserDTO {
    private Integer id;
    private String username;
    private String email;
    private List<Post> posts;
    private List<Post> likedPosts;
    private List<Comment> likedComments;
    private boolean isConfirmed;

     public RegisterResponseUserDTO(User user){
         id=user.getId();
         username=user.getUsername();
         email=user.getEmail();
         posts=user.getPosts();
         likedComments=user.getLikedComments();
         likedPosts = user.getLikedPosts();
         isConfirmed = false;
     }

}
