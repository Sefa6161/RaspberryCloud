package com.Projekt.RaspberryCloud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.Projekt.RaspberryCloud.model.Data;
import com.Projekt.RaspberryCloud.service.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("Data")
public class DataController {
    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping()
    public String startPage() {
        return "Start Seite";
    }

    @GetMapping("/newFile")
    public String newFile() {
        return dataService.saveNewFile();
    }

    @GetMapping("/allData")
    public List<Data> showTable() {
        return dataService.showTable();
    }

}
