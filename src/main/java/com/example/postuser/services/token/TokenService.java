package com.example.postuser.services.token;

import com.example.postuser.model.entities.Token;

import java.util.Optional;

public interface TokenService {

    void saveToken(Token token);

    Optional<Token> getToken(String token);

    int setConfirmedAt(String token);

    void deleteByOwnerId(Integer id);
}
