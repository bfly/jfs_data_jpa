package com.example.jfs_data_jpa;

import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import javax.management.OperationsException;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment =
        SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
public class StudentRestApiTest {

    @LocalServerPort
    private final int port = 8080;

    String baseUrl = "http://localhost:";

    private final TestRestTemplate restTemplate;

    @Autowired
    public StudentRestApiTest(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private ResponseEntity<Student[]> testGetStudents(String url) {
        System.out.println("\n" + url);
        ResponseEntity<Student[]> response =
            restTemplate.getForEntity(url, Student[].class);
        assertNotNull(response);
        assertEquals(Response.SC_OK, response.getStatusCodeValue());
        System.out.println(Arrays.toString(response.getBody()));
        return response;
    }

    @Test
    void testGetAll() {                                                                                 // tested
        String url = baseUrl + port + "/students";
        System.out.println("\n" + url);
        ResponseEntity<Student[]> response = testGetStudents(url);
        System.out.println(Arrays.toString(response.getBody()));
        System.out.println("Success");
    }

    @Test
    void testGetByName() {                                                                              // tested
        String url = baseUrl + port + "/students?name=Palmer";
        System.out.println("\n" + url);
        ResponseEntity<Student[]> response = testGetStudents(url);
        assertEquals(1, Objects.requireNonNull(response.getBody()).length);
        System.out.println(Arrays.toString(response.getBody()));
        System.out.println("Success");
    }

    @Test
    void testGetOne() {                                                                                 // tested
        String url = baseUrl + port + "/students/5";
        System.out.println("\n" + url);
        ResponseEntity<Student> response = restTemplate.getForEntity(url, Student.class);
        assertNotNull(response);
        assertEquals(Response.SC_OK, response.getStatusCodeValue());
        System.out.println(response.getBody());
        System.out.println("Success");
    }

    @Test
    void testNewStudent() {                                                                           // tested
        String url = baseUrl + port + "/students";
        System.out.println("\n" + url);
        Student student = new Student("Bill");
        String response = restTemplate.postForObject(url, student, String.class);
        assertNotNull(response);
        System.out.println(response);
        System.out.println("Success");
    }

    @Test
    void testUpdateStudent() {                                                                  // tested
        String url = baseUrl + port + "/students";
        System.out.println("\n" + url);
        Student student = new Student("Bill");
        String response = restTemplate.postForObject(url, student, String.class);
        assertNotNull(response);
        System.out.println(response);
        int end = response.indexOf(',');
        Long id = Long.valueOf(response.substring(6, end));
        student.setId(id);
        student.setName("Adam");
        response = restTemplate.postForObject(url, student, String.class);
        System.out.println(response);
        System.out.println("Success");
    }

    @Test
    void testDelete() {                                                                        // tested
        int before = getCount();
        String url2 = baseUrl + port + "/students/delete/1";
        System.out.println("\n" + url2);
        restTemplate.delete(url2);
        assertDoesNotThrow(() -> OperationsException.class);
        int after = getCount();
        assertEquals(before - 1, after);                    // Verify delete worked
        System.out.println("Success");
    }

    private int getCount() {                                                                    // tested
        String url2 = baseUrl + port + "/students/count";
        int count = restTemplate.getForObject(url2, Integer.class);
        System.out.printf("%d students\n", count);
        return count;
    }
}

