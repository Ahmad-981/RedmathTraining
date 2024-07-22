package com.practice.project06.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok( authService.authenticate(authRequest));

    }



//    @PostMapping("/register")
//    public String register(@RequestBody User user) {
//        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
//            return "Username already exists";
//        }
//        userRepository.save(user);
//        return "User registered successfully";
//    }





}