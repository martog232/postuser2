package com.example.postuser.model.dto;

import com.example.postuser.model.entities.Comment;
import com.example.postuser.model.entities.Image;
import com.example.postuser.model.entities.Post;
import com.example.postuser.model.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Data
@NoArgsConstructor
public class PostDTO {
    private Integer id;
    private String content;
    private int likes;
    private List<Image> imageList;
    private UserWithoutPassDTO user;
    private List<Comment> commentList;
    private List<User> likers;

    public PostDTO(Post p){
        id=p.getId();
        content=p.getContent();
        likes=p.getLikes();
        imageList=p.getImageList();
        commentList=p.getComments();
        likers=p.getLikers();

    }
}
