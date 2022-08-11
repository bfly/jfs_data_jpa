package com.example.jfs_data_jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private final StudentRepository studentRepository;

    public StudentService(@Autowired StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public Optional<Student> findOne(Long studentId){
        return studentRepository.findById(studentId);
    }

    public List<Student> findByName(String studentName){
        return studentRepository.findByName(studentName);
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long studentId){
        studentRepository.deleteById(studentId);
    }

    public void deleteAll() {
        studentRepository.deleteAll();
    }

    public long count() { return studentRepository.count(); }
}
