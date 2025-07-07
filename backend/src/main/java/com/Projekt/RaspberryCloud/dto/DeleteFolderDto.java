package com.Projekt.RaspberryCloud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteFolderDto {
    private String foldername;
    private String path;
}
