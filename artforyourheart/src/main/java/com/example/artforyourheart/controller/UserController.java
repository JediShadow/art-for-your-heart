package com.example.artforyourheart.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.artforyourheart.cloudinary.CloudinaryService;
import com.example.artforyourheart.model.User;
import com.example.artforyourheart.repository.UserRepository;
import com.example.artforyourheart.service.LikesService;
import com.example.artforyourheart.service.UserService;
import com.example.artforyourheart.service.MatchingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private LikesService likesService;

    @Autowired
    private MatchingService matchingService;

    @Autowired
    private CloudinaryService cloudinaryService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // Get one user (by ID)
    @GetMapping("/{id}")
    public ResponseEntity<?> getOneUser(@PathVariable String id) {
        return userService.findOneUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<List<User>>(userService.allUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable String id) {
        userService.deleteOneUser(id);
        return ResponseEntity.ok().build();
    }

    // Updated an existing user (please note that the server is expecting every field to not be null)
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable ObjectId id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }

    // Register a new user
    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("age") Integer age,
            @RequestParam("height") String height,
            @RequestParam("location") String location,
            @RequestParam("gender") String gender,
            @RequestParam("bio") String bio,
            @RequestParam("realPhoto") MultipartFile realPhoto,
            @RequestParam("artPhotos") List<MultipartFile> artPhotos,
            @RequestParam("interests") List<String> interests,
            @RequestParam(value = "matches", required = false) List<String> matches,
            @RequestParam(value = "yes", required = false) List<String> yes,
            @RequestParam(value = "no", required = false) List<String> no,
            @RequestParam("roles") List<String> roles) throws IOException {

        // Creating user WITHOUT photo URLS first (because we won't have ID until after user is created, and we need the ID for the photo upload)
        User user = userService.createUser(username, password, name, age, height, location, gender, bio, null, null, interests, matches, yes, no, roles);
        // Get the ID from the user after they're created
        String userId = user.getId().toString();
        ObjectId userObjectId = new ObjectId(userId);
        // Uploading the photos to Cloudinary
        String realPhotoUrl = cloudinaryService.uploadRealPhoto(realPhoto, userId);
        List<String> artPhotoUrls = cloudinaryService.uploadArtPhotos(artPhotos, userId);
        // Update user with the photo URLs saved to be used and rendered later
        user.setRealPhoto(realPhotoUrl);
        user.setArtPhotos(artPhotoUrls);
        User updatedUser = userService.updateUser(user.getId(), user);
        return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
    }

    // Get the home screen and matchable users
    @GetMapping("/main")
    public ResponseEntity<List<User>> getUsersToSwipe(@RequestParam String userId) {
        Optional<User> currentUserOptional = userService.findOneUser(userId);
        User currentUser = currentUserOptional.get();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<User> swipeableUsers = matchingService.findCompatibleMatches(userId);
        if (swipeableUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(swipeableUsers);
    }

    // Checks if user is authenticated; redundant now that we know it is implemented correctly (user is only authenticated if they successfully are logged in)
    @GetMapping("/api/auth/check")
    public ResponseEntity<?> checkAuthentication(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAuthenticated = authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();

        return ResponseEntity.ok(isAuthenticated);
    }

}
