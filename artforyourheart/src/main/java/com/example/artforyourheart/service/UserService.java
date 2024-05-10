package com.example.artforyourheart.service;


import com.example.artforyourheart.model.User;
import com.example.artforyourheart.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.List;
import java.util.Optional;
// This service class manages operations related to user entities
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //getOne
    //Optional handles possibility of not being able to find a particular user due to no match
    public Optional<User> findOneUser(String id) {
        return userRepository.findById(id);
    }

    //getAll method to retrieve all users
    public List<User> allUsers() {
        System.out.println(userRepository.findAll());
        return userRepository.findAll();
    }


  
    public User updatedUser(ObjectId id, User updatedUser) {
        // set user ID to make sure it matches the existing user you want to update
        updatedUser.setId(id);
        // save updated user, overwriting the existing user with the same ID
        return userRepository.save(updatedUser);
    }


    //put method to update a user
    public User updateUser(String id, User updatedUser) {
        // Retrieve the existing user from the database
       User existingUser = userRepository.findByUsername(updatedUser.getUsername());
       String password = existingUser.getPassword();
         String realPhotoUrl = existingUser.getRealPhoto(); 
         List<String> artPhotoUrls = existingUser.getArtPhotos();


        // Update the fields of the existing user with the provided updated user
        existingUser.setName(updatedUser.getName());
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setAge(updatedUser.getAge());
        existingUser.setHeight(updatedUser.getHeight());
        existingUser.setLocation(updatedUser.getLocation());
        existingUser.setGender(updatedUser.getGender());
        existingUser.setBio(updatedUser.getBio());
        existingUser.setInterests(updatedUser.getInterests());
        existingUser.setRoles(updatedUser.getRoles());
        existingUser.setPassword(password);
        existingUser.setRealPhoto(realPhotoUrl);
        existingUser.setArtPhotos(artPhotoUrls);
        // Save the updated user
        return userRepository.save(existingUser);
    }

    //deleteOne method to delete a user by ID
    public void deleteOneUser(String id) {
        userRepository.deleteById(id);
    }

    //login method to authenticate a user
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);

        //matches method checks if passwords match
        if (user != null && bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }

    //post register user 
    public User createUser(String username, String password, String name, Integer age, String height, String location, String gender, String bio, String realPhoto, List<String> artPhotos, List<String> interests, List<String> matches, List<String> yes, List<String> no, List<String> roles) {
        // Encode the password before storing it in the database
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        // Create a new user object
        User user = new User(username, encodedPassword, name, age, height, location, gender, bio, realPhoto, artPhotos, interests, matches, yes, no, roles);
        // Save the user to the repository       
        User savedUser = userRepository.insert(user);
        return savedUser;
    }

}
