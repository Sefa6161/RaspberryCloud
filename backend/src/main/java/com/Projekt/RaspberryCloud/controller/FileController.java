package com.Projekt.RaspberryCloud.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Projekt.RaspberryCloud.dto.FileViewDto;
import com.Projekt.RaspberryCloud.service.FileService;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public String showUserFiles(
            @RequestParam(name = "path", required = false, defaultValue = "") String currentPath,
            Model model,
            Authentication authentication) throws AccessDeniedException {

        List<FileViewDto> entries = fileService.getFilesAndFoldersForDisplay(authentication.getName(), currentPath);
        model.addAttribute("entries", entries);
        model.addAttribute("currentPath", currentPath);
        return "file_list";
    }

}
