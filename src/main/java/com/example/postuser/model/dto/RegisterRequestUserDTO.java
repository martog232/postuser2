package com.example.postuser.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequestUserDTO {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
