package com.example.postuser.controllers.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class APIError {
    private String code;
    private String name;
    private String description;
}
