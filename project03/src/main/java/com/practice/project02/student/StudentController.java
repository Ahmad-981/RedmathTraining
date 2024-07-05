package com.practice.project02.student;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;


@RestController
public class StudentController {

    private final StudentServices studentServices;

    public StudentController(StudentServices studentServices) {
        this.studentServices = studentServices;
    }

    @GetMapping("/api/v1/student/{id}")
    public ResponseEntity<Student> get(@PathVariable("id") Long id) {
        Optional<Student> student = studentServices.findById(id);
        if (student.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student.get());
    }

    @GetMapping("/api/v1/student")
    public ResponseEntity<List<Student>> get(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                          @RequestParam(name = "page", defaultValue = "1000") Integer size) {
        return ResponseEntity.ok(studentServices.findAll(page, size));
    }

    @PostMapping("/api/v1/student")
    public ResponseEntity<Student> create(@RequestBody Student student){
        student = studentServices.create(student);
        return ResponseEntity.created(URI.create("/api/v1/student" + student.getId()) ).body(student);
    }
}
