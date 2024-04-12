package com.example.artforyourheart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.artforyourheart.model.Like;
import com.example.artforyourheart.model.User;
import com.example.artforyourheart.service.LikesService;

// Define REST controller and route endpoint
@RestController
@RequestMapping("/likes")
public class LikesController {

    // Inject instance of LikesService
    @Autowired
    private LikesService likesService;

    // Get mapping to retrieve all likes
    @GetMapping
    public ResponseEntity<List<Like>> getAllLikes() {
        return new ResponseEntity<List<Like>>(likesService.allLikes(), HttpStatus.OK);
    }

    // Post mapping to like a user
    @PostMapping
    public ResponseEntity<?> likeUser(@RequestBody Like like) {
        likesService.recordLike(like);
        return ResponseEntity.ok().build();
    }

    // Get mapping to retrieve all matches
    @GetMapping("/matches")
    public ResponseEntity<List<User>> getMatches(@RequestParam String userId) {
        List<User> matches = likesService.findMatches(userId);
        return ResponseEntity.ok(matches);
    }
}
