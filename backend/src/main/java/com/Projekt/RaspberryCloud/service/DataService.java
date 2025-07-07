package com.Projekt.RaspberryCloud.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Projekt.RaspberryCloud.dto.DeleteFileDto;
import com.Projekt.RaspberryCloud.model.Data;
import com.Projekt.RaspberryCloud.repository.DataRepository;
import com.Projekt.RaspberryCloud.util.PathUtils;

import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;

@Service
public class DataService {
    private final DataRepository dataRepository;
    private Path baseDir = Paths.get(System.getProperty("user.home"),
            "RaspberryCloud", "Users");;

    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Transactional
    public String uploadData(String username, MultipartFile[] files, String currentPath) throws IOException {

        Path userDir = baseDir.resolve(username);

        for (MultipartFile file : files) {

            Path target = userDir.resolve(currentPath).resolve(file.getOriginalFilename());

            if (dataRepository.findByNameAndPath(file.getOriginalFilename(), currentPath).isPresent()) {
                throw new EntityExistsException("File already uploaded");
            }
            Files.createDirectories(target.getParent());
            Files.write(target, file.getBytes());

            Data meta = new Data();
            meta.setName(file.getOriginalFilename());
            meta.setPath(PathUtils.normalize(currentPath));
            meta.setAbsolutePath(target.toString());
            meta.setDatatype(file.getContentType());
            meta.setCreationTime(LocalDateTime.now());
            meta.setLastModifiedTime(LocalDateTime.now());
            meta.setUploadUser(username);
            meta.setMemorySize(file.getSize());
            dataRepository.save(meta);
        }
        return "/files" + username;
    }

    public Resource prepareDownload(String username, String currentPath, String name) throws IOException {
        String normalizedPath = PathUtils.normalize(currentPath);

        Data data = dataRepository.findByNameAndPath(name, normalizedPath)
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        Path filePath = Paths.get(data.getAbsolutePath());
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found");
        }

        data.incrementDownloadCounter();
        dataRepository.save(data);

        return new UrlResource(filePath.toUri());
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
                    Data data = dataRepository.findByNameAndPath(filename, fileToDelete.getPath()).get();
                    dataRepository.delete(data);
                    deletedCounter++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return deletedCounter;

    }

    public long getFileCountForUser(String username) {
        return dataRepository.findByUploadUser(username).size();
    }

}
