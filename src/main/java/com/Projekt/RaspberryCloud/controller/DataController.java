package com.Projekt.RaspberryCloud.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Projekt.RaspberryCloud.dto.DataDto;
import com.Projekt.RaspberryCloud.service.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("Data")
public class DataController {
    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping()
    public String startPage() {
        return "Start Seite";
    }

    @PostMapping("/upload")
    public String upload(@RequestPart("file") MultipartFile file, @RequestParam String path,
            @RequestParam("lastModified") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastModified,
            @RequestParam("creationTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime creationTime) {
        return dataService.upload(file, path, lastModified, creationTime);
    }

    @GetMapping("/download")
    public DataDto downloadData(@RequestParam Integer id, @RequestParam String path) {
        return dataService.downloadData(id, path);
    }

}
