package com.Projekt.RaspberryCloud.dto.mapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.Projekt.RaspberryCloud.dto.DataDto;
import com.Projekt.RaspberryCloud.model.Data;

public class DataMapper {
    public static DataDto entityToDto(Data data) {
        DataDto dataDto = new DataDto();
        dataDto.setName(data.getName());

        Path paths = Paths.get(data.getPath(), data.getName());
        try {
            dataDto.setBytes(Files.readAllBytes(paths));
        } catch (Exception e) {
            System.out.println("Fehler beim Einlesen für Download");
        }

        return dataDto;
    }
}
