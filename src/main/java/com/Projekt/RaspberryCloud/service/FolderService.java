package com.Projekt.RaspberryCloud.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.Projekt.RaspberryCloud.dto.DeleteFolderDto;
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

    public List<Folder> listFoldersInPath(String currentPath) {
        return folderRepository.findByPath(currentPath);
    }

    public int deleteFolders(String username, List<DeleteFolderDto> foldersToDelete) {
        int deleteCounter = 0;

        for (DeleteFolderDto folderDto : foldersToDelete) {
            Path folderPath = baseDir.resolve(username).resolve(folderDto.getFoldername());
            Folder deleteFolder = folderRepository.findByFoldername(folderDto.getFoldername()).orElseThrow();
            try {
                deleteDirectoryRecursively(folderPath);
                folderRepository.delete(deleteFolder);
                deleteCounter++;

            } catch (Exception e) {
                throw new RuntimeException("Cannot delete Folder: " + folderPath, e);
            }
        }
        return deleteCounter;
    }

    public static void deleteDirectoryRecursively(Path path) throws IOException {
        if (!Files.exists(path))
            return;

        try (Stream<Path> entries = Files.walk(path)) {
            entries.sorted((a, b) -> b.compareTo(a))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            throw new RuntimeException("Error while deleting: " + p, e);
                        }
                    });
        }
    }

}
