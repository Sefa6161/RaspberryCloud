package com.Projekt.RaspberryCloud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteFileDto {
    private String filename;
    private String path;
}
