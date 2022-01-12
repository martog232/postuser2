package com.example.postuser.model.dto.user;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequestUserDTO {
    private String username;
    private String email;
    private String password;

    private String confirmPassword;
}
