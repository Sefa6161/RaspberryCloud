package com.Projekt.RaspberryCloud.controller;

import java.nio.file.AccessDeniedException;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Projekt.RaspberryCloud.service.FolderService;
import com.Projekt.RaspberryCloud.util.AccessValidator;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("web/user/{username}")
public class FolderController {
    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping("/newFolder")
    public String newFolder(@PathVariable String username,
            @RequestParam String currentPath,
            @RequestParam String folderName,
            RedirectAttributes redirectAttributes,
            Authentication authentication) throws AccessDeniedException {

        if (!AccessValidator.canAccess(username, authentication)) {
            throw new AccessDeniedException("Zugriff verweigert");
        }

        folderService.createNewFolder(username, currentPath, folderName);

        return "redirect:/files/" + username + (currentPath.isEmpty() ? "" : "?path=" + currentPath);

    }

}
