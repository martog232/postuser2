package com.example.postuser.controllers;

import com.example.postuser.model.entities.Image;
import com.example.postuser.model.repositories.ImageRepository;
import com.example.postuser.model.repositories.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

@RestController
@AllArgsConstructor
public class ImageController {

    private static final String ASSETS_DIR = "C:\\Users\\marto\\IdeaProjects\\postuser\\assets";

    private PostRepository postRepository;
    private ImageRepository imageRepository;

    @PutMapping("/posts/{id}/images")
    public Image upload(@PathVariable int id, @RequestPart(name = "file") MultipartFile file) throws IOException {
        //create a physical file
        //write all bytes from the multipartfile
        //create an Image object
        //set its url to the path of the phisical file
        //save object
        //return object

        File pFile = new File(ASSETS_DIR + File.separator + id + "_" + System.nanoTime() + ".png");
        OutputStream os = new FileOutputStream(pFile);
        os.write(file.getBytes());
        Image image = new Image();
        image.setUrl(pFile.getAbsolutePath());
        image.setPost(postRepository.findById(id).get());
        imageRepository.save(image);
        os.close();
        return image;
    }

    @GetMapping(value = "/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] download(@PathVariable int id) throws IOException {
        //find it ,extract its url
        //get the physical file from url
        //read bytes and write them in response
        Image image = imageRepository.findById(id).get();
        String url = image.getUrl();
        File pFile = new File(url);
        return Files.readAllBytes(pFile.toPath());
    }
}
