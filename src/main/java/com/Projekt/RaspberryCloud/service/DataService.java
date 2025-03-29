package com.Projekt.RaspberryCloud.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Projekt.RaspberryCloud.model.Data;
import com.Projekt.RaspberryCloud.repository.DataRepository;

@Service
public class DataService {
    private final DataRepository dataRepository;

    @Autowired
    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public String saveNewFile() {
        Data newData = new Data();
        dataRepository.save(newData);

        return "Speichern erfolgreich";
    }

    public List<String> showTable() {
        List<Data> dataList = dataRepository.findAll();
        List<String> dataListToString = new ArrayList<String>();
        for (Data data : dataList) {
            dataListToString.add(data.getPath() + "/" + data.getName());
        }
        return dataListToString;
    }

    public List<Data> getDataByDatatype(String datatype) {
        return dataRepository.findByDatatype(datatype);
    }

    public String addData(Data data) {
        dataRepository.save(data);
        return "Speichern erfolgreich";
    }

    // ToDO: Es wird nicht gegengecheckt ob die Hochzuladende Datei bereits
    // existiert
    public String upload(MultipartFile file) {
        Data newData;
        try {
            byte[] fileInput = file.getBytes();
            Path filePath = Paths.get("src/main/resources/uploads", file.getOriginalFilename());
            Files.write(filePath, fileInput);
            newData = new Data(null, "pdf", file.getOriginalFilename(), filePath.getParent().toString());
            dataRepository.save(newData);
        } catch (Exception e) {
            System.out.println("Fehler beim Einlesen der Datei");
        }

        return "Datei gespeichert";
    }

}
