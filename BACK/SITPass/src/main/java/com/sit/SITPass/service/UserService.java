package com.sit.SITPass.service;

import com.sit.SITPass.model.User;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    Optional<User> getUserByEmail(String email);
    void sendEmailAboutPassword(User user) throws MessagingException;
    Optional<User> getUserById(Long id);
}
