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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.Projekt.RaspberryCloud.dto.DeleteFileDto;
import com.Projekt.RaspberryCloud.dto.DeleteFolderDto;
import com.Projekt.RaspberryCloud.security.AccessValidator;

@Controller
@RequestMapping("/web/user/{username}")
public class DeleteController {

    private final DataService dataService;
    private final FolderService folderService;

    public DeleteController(DataService dataService, FolderService folderService) {
        this.dataService = dataService;
        this.folderService = folderService;
    }

    @PostMapping("/delete")
    public String deleteData(@PathVariable String username,
            @RequestParam("selectedItems") String selectedItemsJson,
            Authentication authentication,
            RedirectAttributes redirectAttributes) throws AccessDeniedException {

        if (!AccessValidator.canAccess(username, authentication)) {
            throw new AccessDeniedException("Access Denied");
        }

        // read the JSON
        ObjectMapper objectMapper = new ObjectMapper();

        List<DeleteFileDto> filesToDelete = new ArrayList<>();
        List<DeleteFolderDto> foldersToDelete = new ArrayList<>();

        try {
            JsonNode arrayNode = objectMapper.readTree(selectedItemsJson);

            if (arrayNode.isArray()) {
                for (JsonNode node : arrayNode) {
                    if (node.has("type") && node.has("name")) {
                        String type = node.get("type").asText();
                        String name = node.get("name").asText();

                        if ("file".equals(type)) {
                            filesToDelete.add(new DeleteFileDto(name, "Pfad TODO"));
                        } else if ("folder".equals(type)) {
                            foldersToDelete.add(new DeleteFolderDto(name, "Pfad TODO"));
                        }
                    } else {
                        System.out.println("JSON is invalid: " + node);
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error by reading JSON: " + selectedItemsJson, e);
        }

        int deletedData = dataService.deleteData(username, filesToDelete);
        int deletedFolders = folderService.deleteFolders(username, foldersToDelete);
        redirectAttributes.addFlashAttribute("message", deletedData + deletedFolders);

        return "redirect:/files/" + username;
    }
}
