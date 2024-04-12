package com.example.artforyourheart.repository;

import com.example.artforyourheart.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
//This repository is a bridge between artforyourheart and MongoDB for operations related to message entities

//@Repository annotation allows Spring to manage it as a bean
@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    // Custom query method to find a user by their username
    User findByUsername(String username);
    // Custom query method to find a user by their string id
    Optional<User> findById(String id);
    // Custom method to delete a user by their string id
    void deleteById(String id);
}
