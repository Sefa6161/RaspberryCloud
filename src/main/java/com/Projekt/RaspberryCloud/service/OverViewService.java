package com.Projekt.RaspberryCloud.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Projekt.RaspberryCloud.model.Data;
import com.Projekt.RaspberryCloud.repository.DataRepository;

@Service
public class OverViewService {
    private final DataRepository dataRepository;

    public OverViewService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public List<Data> getAllData() {
        List<Data> dataList = dataRepository.findAll();
        return dataList;
    }

    public List<Data> getDataByDatatype(String datatype) {
        return dataRepository.findByDatatype(datatype);
    }

}
