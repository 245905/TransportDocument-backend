package com.tui.transport.services;

import com.tui.transport.models.Attachment;
import com.tui.transport.models.Driver;
import com.tui.transport.repositories.AttachmentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
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
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final SecurityService securityService;
    private final AttachmentRepository attachmentRepository;

    @Value("${files.location}")
    private String fileLocation;
    private final Logger logger = LoggerFactory.getLogger(FileService.class);

    @PostConstruct
    public void init() {
        try {
            Files.createDirectory(Path.of(fileLocation));
        } catch (FileAlreadyExistsException ignored){}
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Resource getFile(String id) {
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

    public Resource saveFile(String ownerToken, MultipartFile file) {
        Driver fileOwner = null;//securityService.getUserFromToken(ownerToken);
        if (fileOwner == null) return null;
        try (InputStream inputStream = file.getInputStream()) {
            String id = UUID.randomUUID().toString();
            Path destination = Paths.get(fileLocation, id);
            Files.copy(inputStream, destination,
                    StandardCopyOption.REPLACE_EXISTING);
            Attachment attachment = new Attachment();
            attachment.setFilename(file.getOriginalFilename());
            attachmentRepository.save(attachment);
            return new UrlResource(destination.toUri());
        } catch (Exception e) {
            logger.error("Error saving file: ", e);
            return null;
        }
    }
}
