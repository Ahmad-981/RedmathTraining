package com.practice.project01.helloworld;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class APIController {

    @GetMapping("api/v1/hello-world")
    public ResponseEntity<Map<String, String>> helloworld(){
        return ResponseEntity.ok(Map.of("Name", "Ahmad"));
    }

    @GetMapping("api/v1/date-time")
    public ResponseEntity<Map<String, Object>> dateTime(){
        return ResponseEntity.ok(Map.of("Sending", LocalDateTime.now()));
    }
}
