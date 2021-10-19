package com.example.postuser.model.dto;

import com.example.postuser.model.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Data
@Component
public class UserLoginDTO {
    private String username;
    private String password;

    public UserLoginDTO(User u){
        username=u.getUsername();
        password=u.getPassword();
    }
}
