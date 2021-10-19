package com.example.postuser.model.dto;

import com.example.postuser.model.entities.Comment;
import com.example.postuser.model.entities.Post;
import com.example.postuser.model.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class RegisterResponseUserDTO {
    private Integer id;
    private String username;
    private String email;
    private List<Post> posts;
    private List<Post> likedPosts;
    private List<Comment> likedComments;

     public RegisterResponseUserDTO(User user){
         id=user.getId();
         username=user.getUsername();
         email=user.getEmail();
         posts=user.getPosts();
         likedComments=user.getLikedComments();
         likedPosts=user.getLikedPosts();
     }

}
