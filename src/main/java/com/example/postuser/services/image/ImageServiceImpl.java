package com.example.postuser.services.image;

import com.example.postuser.model.entities.Image;
import com.example.postuser.model.repositories.ImageRepository;
import com.example.postuser.services.post.PostService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

@Service
@AllArgsConstructor
@Configuration
public class ImageServiceImpl implements ImageService {

    private static final String ASSETS_DIR = new File(".").getAbsolutePath();

    private PostService postService;

    private ImageRepository imageRepository;

    @Override
    public Image upload(Integer id, MultipartFile file) throws IOException {
        File pFile = new File(ASSETS_DIR + File.separator + id + "_" + System.nanoTime() + ".png");
        OutputStream os = new FileOutputStream(pFile);
        os.write(file.getBytes());
        Image image = new Image();
        image.setUrl(pFile.getAbsolutePath());
        image.setPost(postService.mapToEntity(postService.findById(id).get()));
        imageRepository.save(image);
        os.close();
        return image;
    }

    @Override
    public byte[] download(Integer id) throws IOException {
        Image image = imageRepository.findById(id).get();
        String url = image.getUrl();
        File pFile = new File(url);
        return Files.readAllBytes(pFile.toPath());
    }
}
