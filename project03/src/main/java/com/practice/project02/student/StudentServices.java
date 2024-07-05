package com.practice.project02.student;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class StudentServices {

    private final  StudentRepository studentRepository;

    public StudentServices(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    Optional<Student> findById(Long id){
        return studentRepository.findById(id);
    }

    public List<Student> findAll(Integer page, Integer count){
        if (page < 0) {
            page = 0;
        }
        if (count > 500) {
            count = 500;
        }
        return studentRepository.findAll(PageRequest.of(page, count)).getContent();
    }

    public Student create(Student student){
        student.setId(System.currentTimeMillis());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        student.setName(username);
        student.setCgpa("NA");
        student.setUniversity("Lums University");
        return studentRepository.save(student);

    }
}
