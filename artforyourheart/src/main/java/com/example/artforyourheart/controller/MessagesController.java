package com.example.artforyourheart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.artforyourheart.model.Message;
import com.example.artforyourheart.service.MessagesService;

// Define REST controller and route endpoint
@RestController
@RequestMapping("/messages")
public class MessagesController {

    // Dependency injection
    @Autowired
    private MessagesService messagesService;

    // POST mapping to send a message
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        message = messagesService.sendMessage(message);
        return ResponseEntity.ok(message);
    }

    // GET mapping to retrieve messages between different users
    @GetMapping("/{userId1}/{userId2}")
    public ResponseEntity<List<Message>> getMessagesBetweenUsers(@PathVariable String userId1,
            @PathVariable String userId2) {
        List<Message> messages = messagesService.getMessagesBetweenUsers(userId1, userId2);
        return ResponseEntity.ok(messages);
    }
}
