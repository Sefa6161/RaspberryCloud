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
import com.Projekt.RaspberryCloud.util.PathUtils;

@Service
public class FolderService {

    private final FolderRepository folderRepository;
    private final Path baseDir = Paths.get(System.getProperty("user.home"),
            "RaspberryCloud", "Users");

    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public void createNewFolder(String username, String currentPath, String folderName) {
        Path dir = Paths.get(baseDir.toString(), username, currentPath).resolve(folderName).normalize();
        try {
            // Create the physical directory
            Files.createDirectories(dir);

            // Save metadata in database
            Folder newFolder = new Folder();
            newFolder.setFoldername(folderName);
            newFolder.setPath(PathUtils.normalize(currentPath));
            newFolder.setAbsolutePath(dir.toString());
            newFolder.setFolderOwner(username);

            folderRepository.save(newFolder);
        } catch (IOException e) {
            throw new RuntimeException("Error creating Folder: " + dir, e);
        }
    }

    public List<Folder> listFoldersInPath(String currentPath) {
        String normalizedPath = PathUtils.normalize(currentPath);
        return folderRepository.findByPath(normalizedPath);
    }

    public int deleteFolders(String username, List<DeleteFolderDto> foldersToDelete) {
        int deleteCounter = 0;

        for (DeleteFolderDto folderDto : foldersToDelete) {
            String cleanedPath = PathUtils.normalize(folderDto.getPath());
            Path folderPath = baseDir.resolve(username).resolve(cleanedPath).resolve(folderDto.getFoldername())
                    .normalize();
            String fullLogicalPath = Paths.get(cleanedPath, folderDto.getFoldername()).normalize().toString()
                    .replace("\\", "/");

            try {
                // Delete physical directory
                deleteDirectoryRecursively(folderPath);

                // Delete subfolders from DB
                List<Folder> subfolders = folderRepository.findAll().stream()
                        .filter(f -> {
                            String folderFullPath = Paths.get(f.getPath(), f.getFoldername()).normalize().toString()
                                    .replace("\\", "/");
                            return folderFullPath.startsWith(fullLogicalPath);
                        })
                        .toList();

                // Delete root folder from DB
                Folder deleteFolder = folderRepository
                        .findByPathAndFoldername(cleanedPath, folderDto.getFoldername())
                        .orElseThrow(() -> new RuntimeException("folder not found in DB: " + folderDto));

                folderRepository.delete(deleteFolder);
                folderRepository.deleteAll(subfolders);

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
            entries.sorted((a, b) -> b.compareTo(a)) // delete children before parent
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
