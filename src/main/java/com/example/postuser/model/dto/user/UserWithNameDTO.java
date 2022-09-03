package com.example.postuser.model.dto.user;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserWithNameDTO {
    private Integer id;
    private String username;
}

