package com.Projekt.RaspberryCloud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Projekt.RaspberryCloud.model.Folder;
import com.Projekt.RaspberryCloud.model.User;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    Optional<User> findByFoldername(String foldername);

    List<Folder> findByPath(String currentPath);
}
