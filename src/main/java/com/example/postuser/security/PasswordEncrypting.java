package com.example.postuser.security;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class PasswordEncrypting {

    public String encryptingPass(String password) throws NoSuchAlgorithmException {
        MessageDigest m=MessageDigest.getInstance("MD5");
        m.update(password.getBytes());
        byte[] bytes=m.digest();
        StringBuilder s = new StringBuilder();
        for (byte aByte : bytes) {
            s.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return s.toString();}
}

