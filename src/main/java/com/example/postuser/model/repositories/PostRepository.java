package com.example.postuser.model.repositories;

import com.example.postuser.model.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Transactional
    @Modifying
    @Query("delete from Post p where p.id = ?1")
    void deletePostById(Integer id);

    @Query("select p from Post p where p.owner.id=?1 and p.group is null")
    List<Post> getAllByOwnerIdAndNotInGroup(Integer ownerId);

}
