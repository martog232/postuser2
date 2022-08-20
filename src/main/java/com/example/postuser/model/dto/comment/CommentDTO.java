package com.example.postuser.model.dto.comment;

import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.dto.user.UserWithNameDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@NoArgsConstructor
public class CommentDTO {
    private Integer id;
    private UserWithNameDTO owner;
    private PostDTO post;
    private List<UserWithNameDTO> likers;
    private String content;
}
