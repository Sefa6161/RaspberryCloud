package com.Projekt.RaspberryCloud.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Data> showTable() {
        return dataRepository.findAll();
    }

    public List<Data> getDataByDatatype(String datatype) {
        return dataRepository.findByDatatype(datatype);
    }

    public String addData(Data data) {
        dataRepository.save(data);
        return "Speichern erfolgreich";
    }

}
