package com.example.Demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class TextFiles {

    // Saari text project folder ke "uploadS" me save hongi
    private final Path root = Paths.get("uploads").toAbsolutePath().normalize();

    public TextFiles() throws IOException {
        Files.createDirectories(root);
    }

    @PostMapping("/text")
    public ResponseEntity<String> uploadText(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }
            Path destination = root.resolve(Paths.get(file.getOriginalFilename()).getFileName().toString()).normalize();
            if (!destination.startsWith(root)) {
                return ResponseEntity.badRequest().body("invalid file name");
            }
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok().body("File uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("upload failed:" + e.getMessage());
        }
    }

}
