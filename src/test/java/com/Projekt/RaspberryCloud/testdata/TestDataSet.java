package com.Projekt.RaspberryCloud.testdata;

import com.Projekt.RaspberryCloud.model.Folder;
import com.Projekt.RaspberryCloud.model.Data;
import com.Projekt.RaspberryCloud.model.User;
import com.Projekt.RaspberryCloud.repository.FolderRepository;
import com.Projekt.RaspberryCloud.repository.DataRepository;
import com.Projekt.RaspberryCloud.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class TestDataSet {

    private final FolderRepository folderRepository;
    private final DataRepository dataRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TestDataSet(FolderRepository folderRepository,
            DataRepository dataRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.folderRepository = folderRepository;
        this.dataRepository = dataRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner commandLineRunner(TestDataSet testDataSet) {
        return args -> {
            testDataSet.run();
        };
    }

    public void run() {
        userRepository.deleteAll();
        folderRepository.deleteAll();
        dataRepository.deleteAll();

        // Generate test-folders
        User testUser = TestDataGenerator.createUser("sefa_test", "123", passwordEncoder);
        String basePath = Paths.get(System.getProperty("user.home"), "RaspberryCloud", "Users", "sefa_test").toString();
        Folder rootFolder1 = TestDataGenerator.createFolder("", "rootfolder1", basePath + "/rootfolder1", "sefa_test");
        Folder rootFolder2 = TestDataGenerator.createFolder("", "rootfolder2", basePath + "/rootfolder2", "sefa_test");
        Folder subFolder1 = TestDataGenerator.createFolder("rootfolder1", "subfolder1",
                basePath + "/rootfolder1/subfolder1", "sefa_test");
        Folder subFolder2 = TestDataGenerator.createFolder("rootfolder1", "subfolder2",
                basePath + "/rootfolder1/subfolder2", "sefa_test");
        Folder subFolder3 = TestDataGenerator.createFolder("rootfolder2", "subfolder3",
                basePath + "/rootfolder2/subfolder3", "sefa_test");

        // Generate test-files
        Data file1 = TestDataGenerator.createFile("file1.txt", "text/plain", "",
                basePath + "/file1.txt", "sefa_test", 10);
        Data file2 = TestDataGenerator.createFile("file2.txt", "text/plain", "",
                basePath + "/file2.txt", "sefa_test", 15);
        Data file3 = TestDataGenerator.createFile("file3.txt", "text/plain", "rootfolder1",
                basePath + "/rootfolder1/file3.txt", "sefa_test", 20);
        Data file4 = TestDataGenerator.createFile("file4.txt", "text/plain", "rootfolder1",
                basePath + "/rootfolder1/file4.txt", "sefa_test", 25);
        Data file5 = TestDataGenerator.createFile("file5.txt", "text/plain", "rootfolder1/subfolder1",
                basePath + "/rootfolder1/subfolder1/file5.txt", "sefa_test", 30);

        userRepository.save(testUser);
        folderRepository.saveAll(List.of(rootFolder1, rootFolder2, subFolder1, subFolder2, subFolder3));
        dataRepository.saveAll(List.of(file1, file2, file3, file4, file5));

        // Generate folder physically
        createDummyDirectory(rootFolder1.getAbsolutePath());
        createDummyDirectory(rootFolder2.getAbsolutePath());
        createDummyDirectory(subFolder1.getAbsolutePath());
        createDummyDirectory(subFolder2.getAbsolutePath());
        createDummyDirectory(subFolder3.getAbsolutePath());

        // Generate file physically
        createDummyFile(file1.getAbsolutePath(), "content of data 1");
        createDummyFile(file2.getAbsolutePath(), "content of data 2");
        createDummyFile(file3.getAbsolutePath(), "content of data 3");
        createDummyFile(file4.getAbsolutePath(), "content of data 4");
        createDummyFile(file5.getAbsolutePath(), "content of data 5");

        System.out.println("Testdata loaded.");

        // delete folders and files after shutdown the testenvoirment
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Files.walk(Paths.get(basePath))
                        .sorted((a, b) -> b.compareTo(a))
                        .forEach(p -> {
                            try {
                                Files.deleteIfExists(p);
                            } catch (IOException ignored) {
                            }
                        });
            } catch (IOException ignored) {
            }
        }));
    }

    private void createDummyDirectory(String pathStr) {
        try {
            Path path = Paths.get(pathStr);
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException("cant generate directory: " + pathStr, e);
        }
    }

    private void createDummyFile(String filePath, String content) {
        try {
            Path path = Paths.get(filePath);
            Files.write(path, content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("cant generate file: " + filePath, e);
        }
    }
}
