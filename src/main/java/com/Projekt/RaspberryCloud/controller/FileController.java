package com.Projekt.RaspberryCloud.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Projekt.RaspberryCloud.dto.FileViewDto;
import com.Projekt.RaspberryCloud.security.AccessValidator;
import com.Projekt.RaspberryCloud.service.FileService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/{username}")
    public String showUserFiles(@PathVariable String username,
            @RequestParam(name = "path", required = false, defaultValue = "") String currentPath,
            Model model,
            Authentication authentication) throws AccessDeniedException {

        if (!AccessValidator.canAccess(username, authentication)) {
            throw new AccessDeniedException("Access Denied");
        }

        List<FileViewDto> entries = fileService.getFilesAndFoldersForDisplay(username, currentPath);
        model.addAttribute("entries", entries);
        model.addAttribute("currentPath", currentPath);
        return "file_list";
    }

}
