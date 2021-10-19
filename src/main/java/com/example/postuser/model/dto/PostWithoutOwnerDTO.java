package com.example.postuser.model.dto;

import com.example.postuser.model.entities.Comment;
import com.example.postuser.model.entities.Image;
import com.example.postuser.model.entities.Post;
import com.example.postuser.model.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@NoArgsConstructor
@Data
@Component
public class PostWithoutOwnerDTO {
    private Integer id;
    private String content;
    private int likes;
    private List<Image> imageList;

    public PostWithoutOwnerDTO(Post p){
        id=p.getId();
        content=p.getContent();
        likes=p.getLikes();
        imageList=p.getImageList();

    }
}
