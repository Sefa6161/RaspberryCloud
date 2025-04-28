package com.Projekt.RaspberryCloud.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Service;

import com.Projekt.RaspberryCloud.model.Folder;
import com.Projekt.RaspberryCloud.repository.FolderRepository;

@Service
public class FolderService {

    private final FolderRepository folderRepository;
    private final Path baseDir = Paths.get(System.getProperty("user.home"),
            "RaspberryCloud", "Users");

    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public void createNewFolder(String username, String currentPath, String folderName) {
        Path dir = baseDir.resolve(username).resolve(currentPath).resolve(folderName);

        try {
            Files.createDirectory(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Folder newFolder = new Folder();
        newFolder.setFoldername(folderName);
        newFolder.setPath(currentPath);
        newFolder.setAbsolutePath(dir.toString());
        folderRepository.save(newFolder);

    }

    public List<Folder> listFoldersFromDb(String currentPath) {
        return folderRepository.findByPath(currentPath);
    }

}
