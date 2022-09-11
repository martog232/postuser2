package com.example.postuser.model.repositories;

import com.example.postuser.model.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("select c from Comment c where c.post.id = ?1")
    List<Comment> getAllByPostId(Integer postId);

    @Modifying
    @Query("delete from Comment where id=?1")
    void deleteCommentById(Integer id);

    @Transactional
    @Modifying
    @Query("delete from Comment where post.id=?1")
    void deleteAllByPost(Integer id);
}
