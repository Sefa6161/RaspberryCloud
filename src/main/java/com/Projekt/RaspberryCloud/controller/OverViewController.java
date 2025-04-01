package com.Projekt.RaspberryCloud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Projekt.RaspberryCloud.model.Data;
import com.Projekt.RaspberryCloud.service.OverViewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("overview")
public class OverViewController {
    private final OverViewService overViewService;

    @Autowired
    public OverViewController(OverViewService overViewService) {
        this.overViewService = overViewService;
    }

    @GetMapping("allData")
    public List<Data> getAllData() {
        return overViewService.getAllData();
    }

    @GetMapping("/{datatype}")
    public List<Data> getDataByDatatype(@PathVariable String datatype) {
        return overViewService.getDataByDatatype(datatype);
    }

}
