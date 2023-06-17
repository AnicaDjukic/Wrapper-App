package com.wrapper.app.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileHandler {

    private static final String LOCAL_PATH = "src/main/resources/files/";

    public File saveFile(MultipartFile file, String path) {
        File savedFile = new File(path);

        try (OutputStream os = new FileOutputStream(savedFile)) {
            os.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return savedFile;
    }

    public Resource download(String filename) {
        try {
            Path file = Paths.get(LOCAL_PATH)
                    .resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
