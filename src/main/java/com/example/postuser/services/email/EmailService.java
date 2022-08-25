package com.example.postuser.services.email;

public interface EmailService {
    String RegBuildSignUpEmail(String name, String link);
    String ResetPassBuildSignUpEmail(String name, String link);
}
