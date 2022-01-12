package com.example.postuser.model.dto.user;

import com.example.postuser.model.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@Component
@Data
public class UserWithNameDTO {
    private Integer id;
    private String username;

    public UserWithNameDTO(User u){
        id=u.getId();
        username=u.getUsername();
    }
public UserWithNameDTO(UserWithoutPassDTO u){
    id=u.getId();
    username=u.getUsername();
}
}

