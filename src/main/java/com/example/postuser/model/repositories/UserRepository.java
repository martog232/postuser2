package com.example.postuser.model.repositories;

import com.example.postuser.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    public User findByEmail(String email);
    public User findByUsername(String username);
}
