package com.example.postuser.controllers;

import com.example.postuser.model.entities.Image;
import com.example.postuser.services.image.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
@Log4j2
public class ImageController {
    private ImageService imageService;

//    @PostMapping(value = "/posts/{id}/images")
//    public Image upload(@PathVariable Integer id, @RequestPart MultipartFile file) throws IOException {
//
//        return imageService.upload(id, file);
//    }

    @GetMapping(value = "/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] download(@PathVariable Integer id) throws IOException {
        //find it ,extract its url
        //get the physical file from url
        //read bytes and write them in response
        return imageService.download(id);
    }

}
