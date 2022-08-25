package com.example.postuser.model.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Data
@Component
public class DoublePassDTO {
    private String newPass;
    private String confirmNewPass;
}
