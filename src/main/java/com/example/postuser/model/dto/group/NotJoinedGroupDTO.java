package com.example.postuser.model.dto.group;

import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.dto.user.UserWithNameDTO;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class NotJoinedGroupDTO {
    private Integer id;
    private String name;
    private String description;
    private List<UserWithNameDTO> members;
    private List<UserWithNameDTO> admins;
}
