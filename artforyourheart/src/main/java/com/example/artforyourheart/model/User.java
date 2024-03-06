package com.example.artforyourheart.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

//@Data is lombok import that handles getters & setters so that we don't have to list them explicitly
@Data
@NoArgsConstructor
@Document(collection = "user")
public class User {
    @Id
    private ObjectId id;
    private String stringId;
    private String username;
    private String password;
    private String name;
    private Integer age;
    private String height;
    private String location;
    private String gender;
    private String bio;
    private String realPhoto; // in URL format
    List<String> artPhotos = new ArrayList<>(); // as a list of URLS
    List<String> interests = new ArrayList<>();
    List<String> matches = new ArrayList<>();
    List<String> yes = new ArrayList<>();
    List<String> no = new ArrayList<>();
    private List<String> roles = new ArrayList<>();

    public User(String username, String password, String name, Integer age, String height, String location, String gender, String bio, String realPhoto, List<String> artPhotos, List<String> interests, List<String> matches, List<String> yes, List<String> no, List<String> roles) {
        this.id = ObjectId.get();
        this.stringId = id.toHexString();
        this.username = username;
        this.password = password;
        this.name = name;
        this.age = age;
        this.height = height;
        this.location = location;
        this.gender = gender;
        this.bio = bio;
        this.realPhoto = realPhoto;
        this.artPhotos = artPhotos;
        this.interests = interests;
        this.matches = matches;
        this.yes = yes;
        this.no = no;
        this.roles = roles;
    }

    // Helper method to add matches
    public void addMatch(String userId) {
        if (!this.matches.contains(userId)) {
            this.matches.add(userId);
        }
    }
}
