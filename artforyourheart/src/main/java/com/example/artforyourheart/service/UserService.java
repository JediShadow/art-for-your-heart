package com.example.artforyourheart.service;


import com.example.artforyourheart.model.User;
import com.example.artforyourheart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //getOne
    //Optional handles possibility of not being able to find a particular user due to no match
    public Optional<User> findOneUser(String id){
        return userRepository.findById(id);
    }

    //getAll
    public List<User> allUsers(){
        System.out.println(userRepository.findAll());
        return userRepository.findAll();
    }

    //patch route
    //need to discuss

    //deleteOne
    public void deleteOneUser(String id) {
        userRepository.deleteById(id);
    }

    //login
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }



    //post register user
    public User createUser(Integer age, String name, String location, String gender, String art, String artPhotos, String photos, String height, List<String> matches, List<String> yes, List<String> no, String role, String bio, String username, String password){
        User user = new User(age, name, location, gender, art, artPhotos,photos, height ,matches, yes, no, role, bio, username, password);
        User savedUser = userRepository.insert(user);
        return savedUser;
    }

}
