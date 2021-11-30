package com.example.postuser.model.repositories;

import com.example.postuser.model.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    Optional<Token> findByToken(String token);

    @Query("select t from Token t where t.expiresAt <?1")
        void deleteExpiredToken(LocalDateTime localDateTime);
//    @Query("delete from User where Token.expiresAt<:date and id=Token.owner.id")
//    void findAllNotConfirmed(@Param("date")LocalDateTime localDateTime);

    @Modifying
    @Query("UPDATE Token c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);
}
