package com.example.jfs_data_jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class StudentRestApi {

    @Autowired
    StudentService studentService;

    Logger logger = LoggerFactory.getLogger(StudentRestApi.class);

    @GetMapping("/students")                // Get all and by name                          // tested
    public ResponseEntity<List<Student>>
    getStudents(@RequestParam(required = false) String name) {
        try {
            List<Student> students = new ArrayList<>();
            if (name == null) {         // get all students
                logger.info("getStudents");
                students.addAll(studentService.getStudents());
            } else {                    // get students by name
                logger.info("findByName");
                students.addAll(studentService.findByName(name));
            }
            if (students.isEmpty()) {   // No students found
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(students, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/students/{id}")           // Get one
    public ResponseEntity<Student> getStudent(@PathVariable("id") long id) {                 // tested
        logger.info("findOne");
        Optional<Student> studentData = studentService.findOne(id);
        return studentData.map(ResponseEntity::ok)                      // student found
            .orElseGet(() -> ResponseEntity.notFound().build());        // not found
    }

    @GetMapping("/students/count")                                                           // tested
    public ResponseEntity<Integer> getStudentCount() {
        logger.info("count student");
        return new ResponseEntity<>((int) studentService.count(), HttpStatus.OK);
    }

    @RequestMapping(value = "/students", method = {RequestMethod.POST, RequestMethod.PUT})  // tested
    public ResponseEntity<Student> saveStudent(@RequestBody Student student) {
        if (student.getId() == null) {                  // add
            logger.info("add student");
            Student newStudent = studentService.save(student);
            if (student.getId() != null) {              // added
                return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
            } else {                                    // should not occur
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {                                        // update
            logger.info("update student");                                                  // tested
            Optional<Student> studentData = studentService.findOne(student.getId());
            if (studentData.isEmpty()) {                // record to update not found
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {                                    // update record
                return new ResponseEntity<>(studentService.save(student), HttpStatus.OK);
            }
        }
    }

    @DeleteMapping("/students/{id}")                                                 // tested
    public HttpStatus deleteStudent(@PathVariable("id") Long id) {
        logger.info("deleteById: " + id);
        studentService.deleteStudent(id);
        Optional<Student> studentOpt = studentService.findOne(id);
        return studentOpt.isEmpty() ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK;     // not found
    }

}
