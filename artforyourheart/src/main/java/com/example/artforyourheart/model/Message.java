package com.example.artforyourheart.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

//Boilerplate for Getters and Setters
@Data
//Creates a Constructor of the Model with default values (for flexbility)
@NoArgsConstructor
//Indicates this will be stored as a Document in MongoDB collection
@Document(collection = "messages")

//This Model represents messaging between users
public class Message {

    @Id
    private String id;
    private String senderId;
    private String receiverId;
    private String content;
    private Date timestamp;
}