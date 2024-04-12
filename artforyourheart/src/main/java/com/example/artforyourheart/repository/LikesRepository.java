package com.example.artforyourheart.repository;

import com.example.artforyourheart.model.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//This repository is a bridge between artforyourheart and MongoDB for operations related to likes entities

//@Repository annotation allows Spring to manage it as a bean
@Repository
public interface LikesRepository extends MongoRepository<Like, String> {
    //Custom query method that checks whether a like exists
    boolean existsByLikerIdAndLikeeId(String likerId, String likeeId);
    //Custom that retrieves list of likes based on string likedId
    List<Like> findByLikerId(String likerId);
}
