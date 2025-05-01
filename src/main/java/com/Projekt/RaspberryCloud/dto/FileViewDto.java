package com.Projekt.RaspberryCloud.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileViewDto {

    private String name;
    private String datatype;
    private LocalDateTime creationTime;
    private int downloadCounter;
    private boolean isFolder;
}
