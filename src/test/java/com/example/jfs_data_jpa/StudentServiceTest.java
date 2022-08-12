package com.example.jfs_data_jpa;

import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        String url1 = baseURL + "/students/1";
        String url2 = baseURL + "/students";
        System.out.println(url1);
        ResponseEntity<Student> response = restTemplate.getForEntity(url1, Student.class);
        assertNotNull(response);
        Student student = response.getBody();
        assertNotNull(student);
        System.out.println("Before: " + student);

        student.setName("George");
        HttpEntity<Student> request = new HttpEntity<>(student);
        response = restTemplate.exchange(url2, HttpMethod.PUT, request, Student.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
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
        assertNotNull(response.getBody());

        String url2 = baseURL + "/students/{id}";
        Map<String, Long> params = Map.of("id", 5L);
        assertEquals(HttpStatus.OK, restTemplate.exchange(url2, HttpMethod.DELETE, HttpEntity.EMPTY, HttpStatus.class, params).getStatusCode());

        System.out.println("Success");
    }
}
