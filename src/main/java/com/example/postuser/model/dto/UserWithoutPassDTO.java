package com.example.postuser.model.dto;

import com.example.postuser.model.entities.Comment;
import com.example.postuser.model.entities.Post;
import com.example.postuser.model.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Component
@Data
public class UserWithoutPassDTO {
    private Integer id;
    private String username;
    private List<PostWithoutOwnerDTO> posts;
    private List<Post> likedPosts;
    private List<Comment> likedComments;

    public UserWithoutPassDTO(User u){
        id=u.getId();
        username=u.getUsername();
        posts=new ArrayList<>();
        for(Post p:u.getPosts())
            posts.add(new PostWithoutOwnerDTO(p));
        likedPosts=u.getLikedPosts();
        likedComments=u.getLikedComments();
    }
}
