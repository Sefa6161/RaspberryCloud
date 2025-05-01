package com.Projekt.RaspberryCloud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Projekt.RaspberryCloud.model.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    Optional<Folder> findByFoldername(String foldername);

    List<Folder> findByPath(String currentPath);

    Optional<Folder> findByPathAndFoldername(String path, String foldername);
}
