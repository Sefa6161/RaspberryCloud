package com.Projekt.RaspberryCloud.model;

import java.io.File;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Data-Table")
public class Data {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String path;

    private File file;

    // Constructor
    public Data() {
    }

    public Data(Integer id, File file, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.file = file;
    }

    public Data(File file, String name, String path) {
        this.name = name;
        this.path = path;
        this.file = file;
    }

    // Getter and Setter
    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
