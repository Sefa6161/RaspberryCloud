package com.Projekt.RaspberryCloud.service;

import java.nio.file.*;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.Projekt.RaspberryCloud.repository.DataRepository;

@Service
public class DeleteService {

    private final DataRepository dataRepository;
    private final Path baseDir = Paths.get(System.getProperty("user.home"), "RaspberryCloud", "Users");

    public DeleteService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public int deleteSelected(String username, List<String> selectedItems, String currentPath) {
        Path userDir = baseDir.resolve(username);
        int deletedCounter = 0;

        for (String item : selectedItems) {
            try {
                String[] parts = item.split(":");
                boolean isFolder = Boolean.parseBoolean(parts[0]);
                String name = parts[1];

                Path target = userDir.resolve(currentPath).resolve(name).normalize();

                if (isFolder) {
                    // Ordner rekursiv löschen
                    if (Files.exists(target) && Files.isDirectory(target)) {
                        Files.walk(target)
                                .sorted(Comparator.reverseOrder())
                                .forEach(path -> {
                                    try {
                                        Files.delete(path);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });

                        deletedCounter++;
                    }
                } else {
                    if (Files.deleteIfExists(target)) {
                        dataRepository.findByName(name).ifPresent(dataRepository::delete);
                        deletedCounter++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedCounter;
    }
}
