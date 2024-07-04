package com.practice.project02.student;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class StudentController {

    private StudentRepository studentRepository;

    public StudentController(StudentRepository newsRepo){
        this.studentRepository = newsRepo;

    }

    @GetMapping("/api/v1/st/{studentId}")
    public ResponseEntity<Student> get(@PathVariable("studentId") Long studentId){

         Optional<Student> student = studentRepository.findById(studentId);
        if(student.isPresent()){
             return ResponseEntity.ok(student.get());
        }else{
             return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/v1/st")
    public ResponseEntity<List<Student>> getAllNews(){
        return ResponseEntity.ok(studentRepository.findAll());
    }

}