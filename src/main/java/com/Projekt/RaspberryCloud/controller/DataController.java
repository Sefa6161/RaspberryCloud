package com.Projekt.RaspberryCloud.controller;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Projekt.RaspberryCloud.dto.DataDto;
import com.Projekt.RaspberryCloud.service.DataService;
import com.Projekt.RaspberryCloud.util.AccessValidator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("web/user/{username}")
public class DataController {
    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
            @PathVariable String username,
            Authentication authentication,
            RedirectAttributes redirectAttributes) throws AccessDeniedException {

        if (!AccessValidator.canAccess(username, authentication)) {
            throw new AccessDeniedException("Access Denied");
        }

        try {
            dataService.uploadData(username, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("message",
                "File: " + file.getOriginalFilename() + " successfull uploaded");
        return "redirect:/files/" + username;
    }

    @GetMapping("/download")
    public DataDto downloadData(@RequestParam Integer id,
            @RequestParam String path,
            @PathVariable String username) {
        return dataService.downloadData(id, path);
    }

    @GetMapping("/files")
    public ResponseEntity<?> getUserFiles(@PathVariable String username, Authentication authentication) {

        if (!AccessValidator.canAccess(username, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        return ResponseEntity.ok("Here are your files");
    }

}
