package com.practice.project06.publicNewsAPI;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class News {

    @Id
    private Long id;
    private String title;
    private String description;
    //private LocalDatetime dateCreated;
}


