package com.example.postuser.exceptions;

public class PasswordsNotSameException extends RuntimeException{
    public PasswordsNotSameException(String message) {
        super(message);
    }
}
