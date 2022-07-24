package com.example.jfs_data_jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentConfig {
    private static final Logger log = LoggerFactory.getLogger(JfsDataJpaApplication.class);
    @Autowired
    private StudentService studentService;

    @Bean
    public CommandLineRunner demo(StudentRepository repository){
        return (args) -> {
            repository.save(new Student("Bauer"));
            repository.save(new Student("O'Brian"));
            repository.save(new Student("Drexler"));
            repository.save(new Student("Palmer"));
            repository.save(new Student("Dessler"));
            System.out.println(repository.count());
        };
    }
}
