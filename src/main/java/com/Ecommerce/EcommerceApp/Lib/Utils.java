package com.Ecommerce.EcommerceApp.Lib;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public  class Utils {

       public static String uploadImage(MultipartFile image) throws IOException {

        String originalFileName = image.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new IllegalArgumentException("Invalid file name");
        }

        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String fileName = UUID.randomUUID() + extension;

        String rootPath = System.getProperty("user.dir");
        String uploadDir = rootPath + File.separator + "uploads" + File.separator + "images";

        File folder = new File(uploadDir);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new IOException("Failed to create upload directory");
            }
        }

        Path destination = Paths.get(uploadDir, fileName);
        Files.copy(image.getInputStream(), destination);

        return fileName;
    }
    
}
