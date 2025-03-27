package com.Projekt.RaspberryCloud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Projekt.RaspberryCloud.model.Data;

@Repository
public interface DataRepository extends JpaRepository<Data, Integer> {

}
