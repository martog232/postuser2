package com.example.postuser.controllers.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum APIErrorCode {
    METHOD_ARG_NOT_VALID 		("EX-400-1", "Method argument not valid", 	"Invalid arguments are passed to the API endpoint!"),

    ENTITY_NOT_FOUND            ("EX-404-1", "Entity not found",            "An entity could not be found."),

    DUPLICATE_ENTITY	        ("EX-409-1", "Entity already exist", 	    "An entity with this code already exists"),

    UNKNOWN_SERVER_EXCEPTION	("EX-500-1", "Unknown server exception", 	"An unknown server error has occurred!");

    private String code;
    private String message;
    private String description;
}
