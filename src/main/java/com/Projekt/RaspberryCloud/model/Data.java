package com.Projekt.RaspberryCloud.model;

import java.io.File;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Data-Table")
public class Data {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String path;
    private String datatype;
    private int downloadCounter;
    private String uploadUser;

    // Constructor
    public Data() {
    }

    public Data(Integer id, String datatype, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.datatype = datatype;
    }

    public Data(String datatype, String name, String path) {
        this.name = name;
        this.path = path;
        this.datatype = datatype;
    }

}
