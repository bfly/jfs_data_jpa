package com.example.jfs_data_jpa;

import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.RequestEntity.put;

@Transactional
@SpringBootTest
        (webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StudentServiceTest {

    public static final String baseURL = "http://localhost:8080";

    public TestRestTemplate restTemplate;

    @Autowired
    public StudentServiceTest(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Test
    void testNewStudent() {
        String url = baseURL + "/students";
        System.out.println("\n" + url);
        Student student = new Student("Bill");
        ResponseEntity<Student> response = restTemplate.postForEntity(url, student, Student.class);
        assertNotNull(response);
        System.out.println(response.getBody());
        System.out.println("Success");
    }

    @Test
    void testGetStudents() {
        String url = baseURL + "/students";
        System.out.println("\n" + url);
        ResponseEntity<Student[]> response =
                restTemplate.getForEntity(url, Student[].class);
        assertNotNull(response);
        System.out.println(Arrays.toString(response.getBody()));
        System.out.println("Success");
    }

    @Test
    void testFindOne() {
        String url = baseURL + "/students/5";
        System.out.println("\n" + url);
        ResponseEntity<Student> response = restTemplate.getForEntity(url, Student.class);
        assertNotNull(response);
        assertEquals(Response.SC_OK, response.getStatusCodeValue());
        System.out.println(response.getBody());
        System.out.println("Success");
    }

    @Test
    void testFindByName() {
        String url = baseURL + "/students?name=Palmer";
        System.out.println("\n + url");
        ResponseEntity<Student[]> response =
                restTemplate.getForEntity(url, Student[].class);
        assertNotNull(response);
        System.out.println(Arrays.toString(response.getBody()));
        System.out.println("Success");
    }

    @Test
    void testUpdateStudents() {
        String url1 = baseURL + "/students/5";
        System.out.println(url1);
        ResponseEntity<Student> response = restTemplate.getForEntity(url1, Student.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Student student = response.getBody();
        assertNotNull(student);
        System.out.println("Before: " + student);
        student.setName("George");
        String url2 = baseURL + "/students";
        put(url2, student);
        response = restTemplate.getForEntity(url1, Student.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Student student2 = response.getBody();
        assert student2 != null;
        System.out.println("After: " + response.getBody());
        assert Objects.equals(student.getName(), student2.getName());
        System.out.println("Success");
    }

    @Test
    void testDeleteStudent() {
        String url = baseURL + "/students/5";
        System.out.println(url);
        ResponseEntity<Student> response = restTemplate.getForEntity(url, Student.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Student student = response.getBody();
        assertNotNull(student);
        restTemplate.delete(url);
        System.out.println("...");
        response = restTemplate.getForEntity(url, Student.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        System.out.println("Success");
    }
}
