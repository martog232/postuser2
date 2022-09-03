package com.example.postuser.model.dto.group;

import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.dto.user.UserWithNameDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class GroupDTO {
    private Integer id;
    private String name;
    private String description;
    @JsonManagedReference
    private List<PostDTO> posts;
    private List<UserWithNameDTO> members;
    private List<UserWithNameDTO> admins;
}
