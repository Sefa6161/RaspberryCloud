package com.Projekt.RaspberryCloud.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Projekt.RaspberryCloud.dto.DataDto;
import com.Projekt.RaspberryCloud.dto.DeleteFileDto;
import com.Projekt.RaspberryCloud.dto.mapper.DataMapper;
import com.Projekt.RaspberryCloud.model.Data;
import com.Projekt.RaspberryCloud.repository.DataRepository;
import com.Projekt.RaspberryCloud.util.PathUtils;

import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;

@Service
public class DataService {
    private final DataRepository dataRepository;
    private final Path baseDir = Paths.get(System.getProperty("user.home"),
            "RaspberryCloud", "Users");

    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Transactional
    public String uploadData(String username, MultipartFile file, String currentPath) throws IOException {

        Path userDir = baseDir.resolve(username);
        Path target = userDir.resolve(currentPath).resolve(file.getOriginalFilename());

        if (dataRepository.findByName(file.getOriginalFilename()).isPresent()) {
            throw new EntityExistsException("File already uploaded");
        }
        Files.write(target, file.getBytes());

        Data meta = new Data();
        meta.setName(file.getOriginalFilename());
        meta.setPath(PathUtils.normalize(currentPath));
        meta.setAbsolutePath(target.toString());
        meta.setDatatype(file.getContentType());
        meta.setCreationTime(LocalDateTime.now());
        meta.setLastModifiedTime(LocalDateTime.now());
        meta.setUploadUser(username);
        dataRepository.save(meta);
        return "/files" + username;
    }

    public DataDto downloadData(Integer id, String path) {
        // TODO die Datei wird gerade über die ID aus der Datenbank gesucht
        // Der User kennt die ID nicht => neuen Weg implementieren um die Downloaddatei
        // aus der Datenbank zu finden
        Data data = dataRepository.findById(id).orElseThrow(() -> new RuntimeException("Datei nicht in der Datenbank"));
        DataDto dataDto = DataMapper.entityToDto(data);

        // check ob die Datei bereits existiert
        // TODO checken ob lastModified Tag der Datei älter ist => Dann downloaden
        try {
            File file = new File(path, data.getName());
            System.out.println("Path beim Download: " + file.getAbsolutePath());
            System.out.println("Filename: " + data.getName());

            if (file.exists() && !file.isDirectory()) {
                System.out.println("Datei existiert");
                return null;
            }

        } catch (Exception e) {
            System.out.println("Fehler beim checken der Datei");
        }

        // Datei abspeichern in download Ordner und counter hochzählen
        Path filePath = Paths.get(path, dataDto.getName());
        try {
            Files.write(filePath, dataDto.getBytes());
            data.incrementDownloadCounter();
            dataRepository.save(data);
        } catch (Exception e) {
            System.out.println("Datei konnte nicht beschrieben werden");
        }
        return dataDto;
    }

    public int deleteData(String username, List<DeleteFileDto> filesToDelete) {

        Path userDir = baseDir.resolve(username);
        int deletedCounter = 0;

        for (DeleteFileDto fileToDelete : filesToDelete) {
            String filename = fileToDelete.getFilename();
            try {
                Path fileToDeletePath = userDir
                        .resolve(PathUtils.normalize(fileToDelete.getPath()))
                        .resolve(filename)
                        .normalize();
                if (Files.deleteIfExists(fileToDeletePath)) {
                    Data data = dataRepository.findByName(filename).get();
                    dataRepository.delete(data);
                    deletedCounter++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return deletedCounter;

    }

}
