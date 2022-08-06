package com.example.postuser.services.image;

import com.example.postuser.model.entities.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

//    Image upload(Integer id, MultipartFile file) throws IOException;

    void saveImage(Image image);

    byte[] download(Integer id) throws IOException;

    void deleteByPostId(Integer id);

    void deleteImageFromFolder(Integer id);
}
