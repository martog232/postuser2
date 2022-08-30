package com.example.postuser.security;

public interface EmailSender {
    void send(String to, String email,String subject);
}
