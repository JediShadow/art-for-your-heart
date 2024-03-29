//package com.example.artforyourheart.service;
//
//import com.example.artforyourheart.model.User;
//import com.example.artforyourheart.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import java.util.stream.Collectors;
//
//@Service
//public class InternalUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username);
//        //returns a new type of User built into Spring Security
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority(role))
//                .collect(Collectors.toList()));
//    }
//
//}