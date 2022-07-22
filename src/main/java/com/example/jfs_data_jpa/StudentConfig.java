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


            log.info("Students found with method from StudentService");
            log.info("----------------------------------------------");
            studentService.getStudents().forEach(studentOne -> {
                log.info(studentOne.toString());
            });
            log.info("");


            studentService.addStudent(new Student("New Student"));
            log.info("Added one Student here");
            log.info("");


            log.info("Check to see if Student has been added.");
            log.info("---------------------------------------");
            studentService.getStudents().forEach(studentOne -> {
                log.info(studentOne.toString());
            });
            log.info("");


            log.info("Delete Student with id of 3");
            log.info("---------------------------");
            studentService.deleteStudent(3L);
            log.info("");

            log.info("See if Student with id of 3 has been deleted.");
            log.info("---------------------------------------------");
            studentService.getStudents().forEach(studentOne -> {
                log.info(studentOne.toString());
            });
        };
    }
}
