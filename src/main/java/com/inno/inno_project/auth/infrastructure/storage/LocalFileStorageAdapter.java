package com.inno.inno_project.auth.infrastructure.storage;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.inno.inno_project.auth.domain.port.out.FileStoragePort;

@Component
public class LocalFileStorageAdapter implements FileStoragePort {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String store(byte[] data, String originalFileName, String category) {
        try {
            Path categoryDir = Paths.get(uploadDir, category);
            Files.createDirectories(categoryDir);

            String extension = getExtension(originalFileName);
            String uniqueName = UUID.randomUUID() + extension;
            Path filePath = categoryDir.resolve(uniqueName);

            Files.write(filePath, data);

            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Impossible de stocker le fichier : " + originalFileName, e);
        }
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return "";
        return fileName.substring(fileName.lastIndexOf("."));
    }
}