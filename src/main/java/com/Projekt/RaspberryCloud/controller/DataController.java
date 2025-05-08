package com.Projekt.RaspberryCloud.controller;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Projekt.RaspberryCloud.service.DataService;
import com.Projekt.RaspberryCloud.util.AccessValidator;
import com.Projekt.RaspberryCloud.util.PathUtils;

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
    public String upload(@RequestParam("files") MultipartFile[] files,
            @PathVariable String username,
            @RequestParam String currentPath,
            Authentication authentication,
            RedirectAttributes redirectAttributes) throws AccessDeniedException {

        if (!AccessValidator.canAccess(username, authentication)) {
            throw new AccessDeniedException("Access Denied");
        }

        try {
            currentPath = PathUtils.normalize(currentPath);
            dataService.uploadData(username, files, currentPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("message",
                files.length + " file successfull uploaded");
        return "redirect:/files/" + username;
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadData(@PathVariable String username,
            @RequestParam String currentPath,
            @RequestParam String name,
            Authentication authentication) throws AccessDeniedException, IOException {

        if (!AccessValidator.canAccess(username, authentication)) {
            throw new AccessDeniedException("Access Denied");
        }

        Resource resource = dataService.prepareDownload(username, currentPath, name);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }

    @GetMapping("/files")
    public ResponseEntity<?> getUserFiles(@PathVariable String username, Authentication authentication) {

        if (!AccessValidator.canAccess(username, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        return ResponseEntity.ok("Here are your files");
    }

}
