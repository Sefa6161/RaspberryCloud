package com.Projekt.RaspberryCloud.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Projekt.RaspberryCloud.dto.DataDto;
import com.Projekt.RaspberryCloud.dto.mapper.DataMapper;
import com.Projekt.RaspberryCloud.model.Data;
import com.Projekt.RaspberryCloud.repository.DataRepository;

@Service
public class DataService {
    private final DataRepository dataRepository;

    @Autowired
    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    // ToDO: Es wird nicht gegengecheckt ob die Hochzuladende Datei bereits
    // existiert und ob letzte Änderungsdatum neuer ist als die vom abgelegter datei
    public String upload(MultipartFile file, String path) {
        Data newData;
        try {
            Path filePath = Paths.get(path);
            if (!Files.exists(filePath)) {
                File folder = new File(filePath.toString());
                folder.mkdirs();
            }

            filePath = Paths.get(path, file.getOriginalFilename());
            Files.write(filePath, file.getBytes());
            newData = new Data(file.getContentType(),
                    file.getOriginalFilename(),
                    filePath.getParent().toString());

            dataRepository.save(newData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Datei gespeichert";
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
            Path filePath = Paths.get(path);
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

}
