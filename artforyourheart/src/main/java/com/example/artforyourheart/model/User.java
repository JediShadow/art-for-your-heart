package com.example.artforyourheart.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

//@Data is lombok import that handles getters & setters
@Data
//@NoArgsConstructor is setter injection
@NoArgsConstructor
@Document(collection="user")
public class User {
 @Id
 private ObjectId id;
   private Integer age;
    private String name;
    private String location;
    private String gender;
    private String art;
   private String artPhotos;
    private String photos;

    private String height;
    List<String> interests;

    List<String> matches;
    List<String> yes;
    List<String> no;
    private String bio;
    private String username;
   private String password;
 private List<String> roles = new ArrayList<>();

    public User(Integer age, String name, String location, String gender, String art, String artPhotos, String photos, String height, List<String> matches, List<String> yes, List<String> no, String role, String bio, String username, String password, List<String> roles){
     this.age = age;
     this.name = name;
     this.gender = gender;
     this.location = location;
     this.art = art;
     this.artPhotos = artPhotos;
     this.photos= photos;
     this.height=height;
     this.matches=matches;
     this.yes=yes;
     this.no=no;
     this.bio=bio;
     this.username= username;
     this.password= password;
    }


}
