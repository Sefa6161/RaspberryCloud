package com.Projekt.RaspberryCloud.controller;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Projekt.RaspberryCloud.service.DataService;
import com.Projekt.RaspberryCloud.service.FolderService;
import com.Projekt.RaspberryCloud.util.PathUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.Projekt.RaspberryCloud.dto.DeleteFileDto;
import com.Projekt.RaspberryCloud.dto.DeleteFolderDto;

@Controller
@RequestMapping("/web/user")
public class DeleteController {

    private final DataService dataService;
    private final FolderService folderService;

    public DeleteController(DataService dataService, FolderService folderService) {
        this.dataService = dataService;
        this.folderService = folderService;
    }

    @PostMapping("/delete")
    public String deleteData(@RequestParam("selectedItems") String selectedItemsJson,
            Authentication authentication,
            RedirectAttributes redirectAttributes) throws AccessDeniedException {

        // read the JSON
        ObjectMapper objectMapper = new ObjectMapper();

        List<DeleteFileDto> filesToDelete = new ArrayList<>();
        List<DeleteFolderDto> foldersToDelete = new ArrayList<>();
        try {
            JsonNode arrayNode = objectMapper.readTree(selectedItemsJson);

            if (arrayNode.isArray()) {
                for (JsonNode node : arrayNode) {
                    String type = node.path("type").asText(null);
                    String name = node.path("name").asText(null);

                    if (type == null || name == null) {
                        continue;
                    }

                    String rawPath = node.path("path").asText("");
                    String normalizedPath = PathUtils.normalize(rawPath);

                    switch (type) {
                        case "file" -> filesToDelete.add(new DeleteFileDto(name, normalizedPath));
                        case "folder" -> foldersToDelete.add(new DeleteFolderDto(name, normalizedPath));
                        default -> System.out.println("Unknown type in JSON: " + type);
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("error while processing JSON", e);
        }

        int deletedFiles = dataService.deleteData(authentication.getName(), filesToDelete);
        int deletedFolders = folderService.deleteFolders(authentication.getName(), foldersToDelete);
        redirectAttributes.addFlashAttribute("message", deletedFiles + deletedFolders + " elements deleted");

        return "redirect:/files";
    }
}
