package com.example.artforyourheart.cloudinary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.artforyourheart.repository.UserRepository;

@Service
public class CloudinaryService {

    // Initiated via constructor injection
    private final Cloudinary cloudinary;
    private final UserRepository userRepository;

    // Constructor
    public CloudinaryService(Cloudinary cloudinary, UserRepository userRepository) {
        this.cloudinary = cloudinary;
        this.userRepository = userRepository;
    }

    // For the user's "real" profile photo
    public String uploadRealPhoto(MultipartFile file, String userId) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()); // Upload image to
                                                                                                  // Cloudinary
        String realPhotoUrl = (String) uploadResult.get("url"); // Retrieve URL of uploaded image
        // Retrieve user entity if found, add photo URL, save updated user back in db
        userRepository.findById(userId).ifPresent(user -> {
            user.setRealPhoto(realPhotoUrl);
            userRepository.save(user);
        });
        return realPhotoUrl;
    }

    // For the user's multiple art photos
    public List<String> uploadArtPhotos(List<MultipartFile> files, String userId) throws IOException {
        List<String> artPhotoUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = (String) uploadResult.get("url");
            artPhotoUrls.add(url);
        }
        // Update user's art photos URLs
        userRepository.findById(userId).ifPresent(user -> {
            user.getArtPhotos().addAll(artPhotoUrls);
            userRepository.save(user);
        });
        return artPhotoUrls;
    }
}