package com.tui.transport.services;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService{
    @Value("${files.location}")
    private String fileLocation;
    private final Logger logger = LoggerFactory.getLogger(FileService.class);

    @PostConstruct
    public void init(){
        try {
            Files.createDirectory(Path.of(fileLocation));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Resource getFile(String id){
        try {
            Path file = Paths.get(fileLocation, id);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public Resource saveFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            Path destination = Paths.get(fileLocation, file.getOriginalFilename());
            Files.copy(inputStream, destination,
                    StandardCopyOption.REPLACE_EXISTING);
            return new UrlResource(destination.toUri());
        }
        catch (Exception e){
            logger.error("Error saving file: ",e);
            return null;
        }
    }
}
