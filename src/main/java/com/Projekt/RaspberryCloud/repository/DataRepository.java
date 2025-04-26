package com.Projekt.RaspberryCloud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Projekt.RaspberryCloud.model.Data;

@Repository
public interface DataRepository extends JpaRepository<Data, Integer> {

    List<Data> findByDatatype(String datatype);

    Optional<Data> findByName(String name);

    List<Data> findByUploadUser(String uploadUser);

}
