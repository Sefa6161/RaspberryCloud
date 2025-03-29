package com.Projekt.RaspberryCloud.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Projekt.RaspberryCloud.model.Data;
import com.Projekt.RaspberryCloud.service.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @GetMapping("/allData")
    public List<Data> showTable() {
        return dataService.showTable();
    }

    @GetMapping("/{datatype}")
    public List<Data> getDataByDatatype(@PathVariable String datatype) {
        return dataService.getDataByDatatype(datatype);
    }

    @PostMapping("/upload")
    public String upload(@RequestPart("file") MultipartFile file) {
        return dataService.upload(file);
    }

}
