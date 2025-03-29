package com.Projekt.RaspberryCloud.service;

import java.util.ArrayList;
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

}
