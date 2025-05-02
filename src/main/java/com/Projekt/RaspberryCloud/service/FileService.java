package com.Projekt.RaspberryCloud.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.Projekt.RaspberryCloud.repository.FolderRepository;
import com.Projekt.RaspberryCloud.util.PathUtils;

import org.springframework.stereotype.Service;

import com.Projekt.RaspberryCloud.dto.FileViewDto;
import com.Projekt.RaspberryCloud.model.Data;
import com.Projekt.RaspberryCloud.model.Folder;
import com.Projekt.RaspberryCloud.repository.DataRepository;

@Service
public class FileService {

    private final FolderRepository folderRepository;
    private final DataRepository dataRepository;

    private final Path baseDir = Paths.get(System.getProperty("user.home"),
            "RaspberryCloud", "Users");

    public FileService(DataRepository dataRepository, FolderRepository folderRepository) {
        this.dataRepository = dataRepository;
        this.folderRepository = folderRepository;
    }

    public List<Data> listFilesFromDb(String username) {
        return dataRepository.findByUploadUser(username);
    }

    public List<String> listFiles(String username) {
        Path userDir = baseDir.resolve(username);
        if (!Files.exists(userDir) || !Files.isDirectory(userDir)) {
            throw new RuntimeException("User-Folder not found: " + userDir);
        }

        try (Stream<Path> stream = Files.list(userDir)) {
            return stream
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error reading Path", e);
        }

    }

    public List<FileViewDto> getFilesAndFoldersForDisplay(String username, String currentPath) {
        List<FileViewDto> entries = new ArrayList<>();

        // display files
        List<Data> userFiles = dataRepository.findByUploadUser(username);
        for (Data file : userFiles) {
            if (file.getPath().endsWith(currentPath)) {
                entries.add(new FileViewDto(
                        file.getName(),
                        file.getDatatype(),
                        file.getCreationTime(),
                        file.getDownloadCounter(),
                        false));
            }
        }

        // display folders
        String normalizedPath = PathUtils.normalize(currentPath);
        List<Folder> folders = folderRepository.findByPath(normalizedPath);
        for (Folder folder : folders) {
            entries.add(new FileViewDto(folder.getFoldername(),
                    "Folder",
                    null,
                    0,
                    true));
        }

        return entries;
    }

}
