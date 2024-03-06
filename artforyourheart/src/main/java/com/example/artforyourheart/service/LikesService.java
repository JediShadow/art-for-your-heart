package com.example.artforyourheart.service;

import com.example.artforyourheart.model.Like;
import com.example.artforyourheart.model.User;
import com.example.artforyourheart.repository.LikesRepository;
import com.example.artforyourheart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikesService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikesRepository likesRepository;

    // Get every record of every like ever made
    public List<Like> allLikes() {
        System.out.println(likesRepository.findAll());
        return likesRepository.findAll();
    }

    // Records likes and adds matches to User repo
    public void recordLike(Like like) {
        if (!likesRepository.existsByLikerIdAndLikeeId(like.getLikerId(), like.getLikeeId())) {
            likesRepository.save(like);
            // Checking for a mutual like
            if (likesRepository.existsByLikerIdAndLikeeId(like.getLikeeId(), like.getLikerId())) {
                // If the like is mutual, then save it under each User
                User liker = userRepository.findById(like.getLikerId()).orElse(null);
                User likee = userRepository.findById(like.getLikeeId()).orElse(null);
                if (liker != null && likee != null) {
                    liker.addMatch(likee.getId().toString());
                    likee.addMatch(liker.getId().toString());
                    userRepository.save(liker);
                    userRepository.save(likee);
                }
            }
        }
    }

    // Might be redundant
    public List<User> findMatches(String userId) {
        List<Like> likesGiven = likesRepository.findByLikerId(userId);
        return likesGiven.stream()
                .filter(like -> likesRepository.existsByLikerIdAndLikeeId(like.getLikeeId(), userId))
                .map(like -> userRepository.findById(like.getLikeeId()).orElse(null))
                .collect(Collectors.toList());
    }

}
