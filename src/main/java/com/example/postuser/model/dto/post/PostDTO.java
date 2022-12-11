package com.example.postuser.model.dto.post;

import com.example.postuser.model.dto.comment.CommentDTO;
import com.example.postuser.model.dto.group.GroupDTO;
import com.example.postuser.model.dto.group.NotJoinedGroupDTO;
import com.example.postuser.model.dto.image.ImageDTO;
import com.example.postuser.model.dto.user.UserWithNameDTO;
import com.example.postuser.model.dto.user.UserWithoutPassDTO;
import com.example.postuser.model.entities.Comment;
import com.example.postuser.model.entities.Image;
import com.example.postuser.model.entities.Post;
import com.example.postuser.model.entities.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Integer id;
    private String content;
    private List<Image> imageList;
    private UserWithNameDTO owner;
    @JsonBackReference
    private GroupDTO group;
    private List<UserWithNameDTO> likers;
    private List<CommentDTO> comments;
    private LocalDateTime created;

}
