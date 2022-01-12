package com.example.postuser.model.dto.user;

import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.dto.post.PostWithoutOwnerDTO;
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
    private List<PostDTO> likedPosts;
    private List<Comment> likedComments;
}
