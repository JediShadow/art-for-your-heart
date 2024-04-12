package com.example.artforyourheart.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//Boilerplate for Getters and Setters
@Data
//Creates a Constructor of the Model with default values (for flexbility)
@NoArgsConstructor
//Indicates this will be stored as a Document in MongoDB collection
@Document(collection="likes")

//This Model represents like interactions between users
public class Like {
    @Id
    private String id;
    private String likerId;
    private String likeeId;
}
