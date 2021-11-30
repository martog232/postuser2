package com.example.postuser.model.repositories;

import com.example.postuser.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u where u.email = ?1")
    User findByEmail(String email);

    @Query("select u from User u where u.username = ?1")
    User findByUsername(String username);

    @Query("select u.isConfirmed from User u where u.email = ?1 or u.username=?2")
    Boolean getIsConfirmedByEmailOrUsername(String email,String username);

    @Modifying
    @Query("delete from User u where u.id = ?1")
    void deleteUserById(Integer id);

    @Query("delete from User u where u.isConfirmed=false ")
    void deleteNotConfirmedUsers();

    @Modifying
    @Query("UPDATE User a " +
            "SET a.isConfirmed = TRUE WHERE a.email = ?1")
    int enableUser(String email);

    @Query("select u.isConfirmed from User u where u.username=?2")
    Boolean getIsConfirmedByUsername(String username);
}
