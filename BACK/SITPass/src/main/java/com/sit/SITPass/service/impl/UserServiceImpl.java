package com.sit.SITPass.service.impl;

import com.sit.SITPass.model.AccountRequest;
import com.sit.SITPass.model.RequestStatus;
import com.sit.SITPass.model.User;
import com.sit.SITPass.repository.UserRepository;
import com.sit.SITPass.service.AccountRequestService;
import com.sit.SITPass.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRequestService accountRequestService;

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void sendEmailAboutPassword(User user) throws MessagingException {

        String body = "Dear user,\nyour password has been changed. If this action wasn't done by you please contact us as soon as possible to retrieve your account. If otherwise, please ignore this email. \n\n\n\n Your SITPass Team.";

        accountRequestService.sendMail(user.getEmail(), "Your SITPASS account password has been changed", body);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
