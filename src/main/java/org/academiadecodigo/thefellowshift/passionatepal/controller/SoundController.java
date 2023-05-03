package org.academiadecodigo.thefellowshift.passionatepal.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;

@RestController
@RequestMapping("/sound")
public class SoundController {


    @GetMapping(value = "/{filename:.+}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> playSound(@PathVariable String filename) throws FileNotFoundException {


        File soundFile = new File(Paths.get(".").toAbsolutePath().normalize() + File.separator + "sound" + File.separator + filename);
        System.out.println(soundFile.getAbsolutePath());
        // Check if the file exists
        if (!soundFile.exists()) {
            System.out.println("doesnt exist");
            return ResponseEntity.notFound().build();
        }
        // Create an input stream from the file
        InputStream inputStream = new FileInputStream(soundFile);

        // Create an input stream resource from the input stream
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        // Create a content disposition header with the attachment type and the filename
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(filename)
                .build();

        // Create a response headers object with the content disposition header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);

        // Return the input stream resource as a response entity with the appropriate content type, length, and headers
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(soundFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStreamResource);
    }
}
