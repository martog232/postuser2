package com.example.postuser.model.dto.post;

import com.example.postuser.model.dto.user.UserWithNameDTO;
import com.example.postuser.model.dto.user.UserWithoutPassDTO;
import com.example.postuser.model.entities.Comment;
import com.example.postuser.model.entities.Image;
import com.example.postuser.model.entities.Post;
import com.example.postuser.model.entities.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
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
    private List<Image> imageList;
    private List<UserWithNameDTO> likers;
    private List<Comment> comments;
    private UserWithNameDTO owner;
}
