package com.example.artforyourheart.repository;

import com.example.artforyourheart.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUsername(String username);

    Optional<User> findById(String id);

    void deleteById(String id);
}
