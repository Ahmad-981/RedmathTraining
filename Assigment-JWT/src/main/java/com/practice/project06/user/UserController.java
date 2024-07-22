package com.practice.project06.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).get();
    }


    @PostMapping("/register")
    public User createUser(@RequestBody User user) {
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

//    @PutMapping("/{id}")
//    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
//        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
//        user.setUsername(userDetails.getUsername());
//        user.setPassword(userDetails.getPassword());
//        user.setRole(userDetails.getRole());
//        user.setName(userDetails.getName());
//        user.setEmail(userDetails.getEmail());
//        user.setAddress(userDetails.getAddress());
//        return userRepository.save(user);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
//        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
//        userRepository.delete(user);
//        return ResponseEntity.ok().build();
//    }
}
