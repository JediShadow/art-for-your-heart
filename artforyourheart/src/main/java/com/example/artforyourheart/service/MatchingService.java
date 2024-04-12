package com.example.artforyourheart.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.artforyourheart.model.User;
import com.example.artforyourheart.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
// This service class manages operations related to finding compatible matches for users
@Service
public class MatchingService {

    @Autowired
    private UserService userService;


    // Method to find compatible matches for a given user
    public List<User> findCompatibleMatches(String userId) {
        // Retrieve the current user by their ID
        Optional<User> currentUserOptional = userService.findOneUser(userId);
        // Get the current user object
        User currentUser = currentUserOptional.get();
        // Check if the current user exists
        if (currentUser == null) {
            // If the current user does not exist, return an empty list
            return List.of();
        }
        // If the current user exists, proceed to fetch all users
        List<User> allUsers = userService.allUsers();
        return allUsers.stream()
                //removes self from string of people
                .filter(user -> !user.getId().equals(currentUser.getId()))
                .collect(Collectors.toList());
    }
}
