package com.practice.project02.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("/api/v1/users")
    public ResponseEntity<List<User>> get(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                             @RequestParam(name = "page", defaultValue = "1000") Integer size) {
        return ResponseEntity.ok(userService.findAll(page, size));
    }

}
