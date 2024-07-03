package com.practice.project01.randomAPI;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class randomAPIController {

    @GetMapping("/")
    public String index(){
        return "index";
    }
}
