package com.practice.project02.student;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Student {

    @Id
    private Long studentId;
    private String name;
    private String rollnum;
    private String university;
    private String cgpa;
}
