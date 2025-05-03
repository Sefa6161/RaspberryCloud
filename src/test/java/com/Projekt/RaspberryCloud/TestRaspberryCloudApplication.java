package com.Projekt.RaspberryCloud;

import com.Projekt.RaspberryCloud.testdata.TestDataSet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

@TestConfiguration(proxyBeanMethods = false)
public class TestRaspberryCloudApplication {

    @ServiceConnection
    @Bean
    public PostgreSQLContainer<?> postgresContainer() {
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.2")
                .withUsername("postgres")
                .withPassword("secret");
        container.setPortBindings(List.of("7432:5432"));
        return container;
    }

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "test");
        SpringApplication
                .from(RaspberryCloudApplication::main)
                .with(TestRaspberryCloudApplication.class, TestDataSet.class)
                .run(args);
    }
}
