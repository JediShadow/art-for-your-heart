package com.example.artforyourheart.service;

import com.example.artforyourheart.model.Message;
import com.example.artforyourheart.repository.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
// This service class manages operations related to sending and retrieving messages
@Service
public class MessagesService {

    @Autowired
    private MessagesRepository messagesRepository;

    // Send a message to a specific User
    public Message sendMessage(Message message) {
        message.setTimestamp(new Date()); // Set the current time as the timestamp
        return messagesRepository.save(message);
    }
    // Method to retrieve messages between two users
    public List<Message> getMessagesBetweenUsers(String userId1, String userId2) {
        return messagesRepository.findMessagesBetweenTwoUsers(userId1, userId2);
    }
}