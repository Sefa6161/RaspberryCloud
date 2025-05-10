package com.Projekt.RaspberryCloud.testdata;

import com.Projekt.RaspberryCloud.model.Data;
import com.Projekt.RaspberryCloud.model.Folder;
import com.Projekt.RaspberryCloud.model.User;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;

public class TestDataGenerator {

    public static User createUser(String username, String rawPassword, PasswordEncoder encoder) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(rawPassword));
        user.setEmail(username + "@test.local");
        user.setAdminFlag(false);
        return user;
    }

    public static Folder createFolder(String path, String name, String absPath) {
        Folder folder = new Folder();
        folder.setPath(path);
        folder.setFoldername(name);
        folder.setAbsolutePath(absPath);
        return folder;
    }

    public static Data createFile(String name, String type, String path, String absPath, String user, long size) {
        Data file = new Data();
        file.setName(name);
        file.setDatatype(type);
        file.setPath(path);
        file.setAbsolutePath(absPath);
        file.setCreationTime(LocalDateTime.now());
        file.setLastModifiedTime(LocalDateTime.now());
        file.setUploadUser(user);
        file.setMemorySize(size);
        return file;
    }
}
