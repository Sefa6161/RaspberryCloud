package com.Projekt.RaspberryCloud.controller;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Projekt.RaspberryCloud.dto.DataDto;
import com.Projekt.RaspberryCloud.security.AccessValidator;
import com.Projekt.RaspberryCloud.service.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("web/user")
public class DataController {
    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping()
    public String startPage() {
        return "Start Seite";
    }

    @PostMapping("/{username}/upload")
    public String upload(@RequestPart("file") MultipartFile file, @RequestParam String path,
            @RequestParam("lastModified") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastModified,
            @RequestParam("creationTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime creationTime,
            @PathVariable String username) {
        return dataService.upload(file, path, lastModified, creationTime);
    }

    @GetMapping("/{username}/download")
    public DataDto downloadData(@RequestParam Integer id,
            @RequestParam String path,
            @PathVariable String username) {
        return dataService.downloadData(id, path);
    }

    @GetMapping("/{username}/files")
    public ResponseEntity<?> getUserFiles(@PathVariable String username, Authentication authentication) {

        if (!AccessValidator.canAccess(username, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Zugriff verweigert.");
        }

        return ResponseEntity.ok("Hier sind deine Dateien");
    }

}
