package com.example.postuser.model.dto.user;

import com.example.postuser.model.dto.comment.CommentDTO;
import com.example.postuser.model.dto.group.GroupDTO;
import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.entities.Group;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class UserWithoutPassDTO {
    private Integer id;
    private String username;
    private List<PostDTO> posts;
    private List<UserWithNameDTO> followers;
    private List<UserWithNameDTO> followings;
    private List<PostDTO> likedPosts;
    private List<CommentDTO> likedComments;
    private List<GroupDTO> groupMember;
    private List<GroupDTO> groupAdmin;
}
