package com.sit.SITPass.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping(value = "/api/images")
public class ImageController {

    @Value("${upload.dir}")
    private String uploadDir;

    @PostMapping(value = "/serve", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Resource> serveFile(@RequestBody String filename) {
        try {
            filename = filename.trim();

            // Save the uploadDir as a variable to use it multiple times later
            String uploadDirPath = uploadDir;

            // Check if the filename starts with "/" and remove it if necessary
            if (filename.startsWith("/")) {
                filename = filename.substring(1);
            }

            // Create the full path of the file by combining uploadDir and filename
            Path filePath = Paths.get(filename).normalize();

            // Check if the path is within the current directory to prevent attacks
            if (!filePath.startsWith(Paths.get("."))) {
                filePath = Paths.get(".", filename).normalize();
            }

            // Create a Resource object from the filePath
            Resource resource = new UrlResource(filePath.toUri());

            System.out.println("Filename : " + filename);
            System.out.println("File path : " + filePath);
            System.out.println("Resource : " + resource);

            // Check if the file exists and is readable
            if (resource.exists() && resource.isReadable()) {
                // Construct the response with the Resource as the body
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                // If the file does not exist or is not readable, return a 404 Not Found response
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            // If a MalformedURLException occurs, return a 400 Bad Request response
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
