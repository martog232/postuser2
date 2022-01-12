package com.example.postuser.model.repositories;

import com.example.postuser.model.entities.Image;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image,Integer> {

}
