package com.example.postuser.model.repositories;

import com.example.postuser.model.entities.Image;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ImageRepository extends JpaRepository<Image,Integer> {
    @Modifying
    @Transactional
    @Query("delete from Image i where i.post.id=?1")
    void deleteByPostId(Integer id);
}
