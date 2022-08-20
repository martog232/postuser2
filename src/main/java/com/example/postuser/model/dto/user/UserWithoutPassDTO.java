package com.example.postuser.model.dto.user;

import com.example.postuser.model.dto.comment.CommentDTO;
import com.example.postuser.model.dto.post.PostDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
@Data
public class UserWithoutPassDTO {
    private Integer id;
    private String username;
    private List<PostDTO> posts;
    private List<UserWithNameDTO> followers;
    private List<UserWithNameDTO> followings;
    private List<PostDTO> likedPosts;
    private List<CommentDTO> likedComments;
}
