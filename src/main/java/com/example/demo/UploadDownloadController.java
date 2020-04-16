package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UploadDownloadController {
    private final SftpService sftpService;

    @PostMapping("/upload")
    public String upload(MultipartFile file){
      log.info("Filename: " + file.getName());
      log.info("Filename: " + file.getOriginalFilename());
        try {
            sftpService.uploadFile(file.getOriginalFilename(), file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Done uploading");
        return "File uploaded... Check the logs";
    }

    @GetMapping("/download/{filename}")
    public Resource download(@PathVariable String filename){
        log.info("Download BEGIN");
        val tmpFilename = sftpService.download(filename);
        log.info(tmpFilename);
        val filepath = Path.of(tmpFilename);
        UrlResource resource = null;
        try {
            resource = new UrlResource(filepath.toUri());
//            new File(tmpFilename).delete();
            log.info("Download END");
            return resource;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
