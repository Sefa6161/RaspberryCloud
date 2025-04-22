package com.Projekt.RaspberryCloud.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.Projekt.RaspberryCloud.model.Data;
import com.Projekt.RaspberryCloud.repository.DataRepository;

@Service
public class FileService {

    private final Path baseDir = Paths.get(System.getProperty("user.home"),
            "RaspberryCloud", "Users");
    private final DataRepository dataRepository;

    public FileService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public List<Data> listFilesFromDb(String username) {
        return dataRepository.findByUploadUser(username);
    }

    public List<String> listFiles(String username) {
        Path userDir = baseDir.resolve(username);
        if (!Files.exists(userDir) || !Files.isDirectory(userDir)) {
            throw new RuntimeException("User-Ordner nicht gefunden: " + userDir);
        }

        try (Stream<Path> stream = Files.list(userDir)) {
            return stream
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .sorted() // optional alphabetisch sortieren
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Lesen des Verzeichnisses", e);
        }

    }

}
