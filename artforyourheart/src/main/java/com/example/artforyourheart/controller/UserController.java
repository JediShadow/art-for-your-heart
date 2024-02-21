package com.example.artforyourheart.controller;


import com.example.artforyourheart.model.User;
import com.example.artforyourheart.repository.UserRepository;
import com.example.artforyourheart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    //get one
    @GetMapping("/{id}")
    public ResponseEntity<?> getOneUser(@PathVariable String id){
        return userService.findOneUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //get all
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<List<User>>(userService.allUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable String id) {
        userService.deleteOneUser(id);
        return ResponseEntity.ok().build();
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User credentials) {
        User user = userService.login(credentials.getUsername(), credentials.getPassword());

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }





    //post register user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody Map<String, Object> payload) {
        Integer age = (Integer) payload.get("age");
        String name = (String) payload.get("name");
        String location = (String) payload.get("location");
        String gender = (String) payload.get("gender");
        String art = (String) payload.get("art");
        String artPhotos = (String) payload.get("artPhotos");
        String photos = (String) payload.get("photos");
        String height = (String) payload.get("height");
        List<String> interests = (List<String>) payload.get("interests");
        List<String> matches = (List<String>) payload.get("matches");
        List<String> yes = (List<String>) payload.get("yes");
        List<String> no = (List<String>) payload.get("no");
        String role = (String) payload.get("role");
        String bio = (String) payload.get("bio");
        String username = (String) payload.get("username");
        String password = (String) payload.get("password");

        User user = userService.createUser(age, name, location, gender, art, artPhotos,photos, height ,matches, yes, no, role, bio, username, password) ;
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

}
