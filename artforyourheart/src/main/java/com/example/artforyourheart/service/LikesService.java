package com.example.artforyourheart.service;

import com.example.artforyourheart.model.Like;
import com.example.artforyourheart.model.User;
import com.example.artforyourheart.repository.LikesRepository;
import com.example.artforyourheart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
// This service class manages operations related to likes
@Service
public class LikesService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikesRepository likesRepository;

    //getAll  method to retrieve all likes
    public List<Like> allLikes(){
        // Print all likes to console for debugging purposes
        System.out.println(likesRepository.findAll());
        // Return all likes
        return likesRepository.findAll();
    }

    //Method to record a like
    public void recordLike(Like like) {
        // Check if the like already exists
        if (!likesRepository.existsByLikerIdAndLikeeId(like.getLikerId(), like.getLikeeId())) {
            // If the like does not exist, save it
            likesRepository.save(like);
        }
    }
    // Method to find matches for a user based on likes
    public List<User> findMatches(String userId) {
        // Retrieve all likes given by the user
        List<Like> likesGiven = likesRepository.findByLikerId(userId);
        // Filter likes to find mutual likes (where the other user also likes the current user)
        return likesGiven.stream()
                .filter(like -> likesRepository.existsByLikerIdAndLikeeId(like.getLikeeId(), userId))
                // Map liked users to their corresponding User objects
                .map(like -> userRepository.findById(like.getLikeeId()).orElse(null))
                // Collect results into a list
                .collect(Collectors.toList());
    }

}
//MVP