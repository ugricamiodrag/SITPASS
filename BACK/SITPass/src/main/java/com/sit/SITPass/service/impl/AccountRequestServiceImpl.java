package com.sit.SITPass.service.impl;

import com.sit.SITPass.model.AccountRequest;
import com.sit.SITPass.model.RequestStatus;
import com.sit.SITPass.model.User;
import com.sit.SITPass.repository.AccountRequestCustomRepository;
import com.sit.SITPass.repository.AccountRequestRepository;
import com.sit.SITPass.repository.UserRepository;
import com.sit.SITPass.service.AccountRequestService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class AccountRequestServiceImpl implements AccountRequestService {

    @Autowired
    private AccountRequestRepository accountRequestRepository;

    @Autowired
    private AccountRequestCustomRepository accountRequestCustomRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AccountRequestServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public boolean emailExists(String email) {
        return accountRequestRepository.existsByEmail(email);
    }

    @Override
    public void createRequest(AccountRequest request) {
        accountRequestRepository.save(request);
    }

    @Override
    public Page<AccountRequest> getRequests(int page, int size) {
        return accountRequestRepository.findAccountRequestsByRequestStatusEquals(RequestStatus.PENDING, PageRequest.of(page, size));
    }

    @Override
    public void sendMail(String email, String subject, String body) throws MessagingException {
        System.out.println("Sending the email!");
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("sitpassOfficial@outlook.com");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(body);

        javaMailSender.send(message);
    }

    @Override
    public void acceptRequest(String email) throws MessagingException {
        String uuid = UUID.randomUUID().toString();
        String body = String.format("Dear user, we are pleased to inform you that your account request has been approved. To access your account, please use the following temporary password: %s. Upon logging in, you will be prompted to change your password and complete your profile setup. We wish you a pleasant experience with SITPASS.", uuid);
        AccountRequest request = accountRequestRepository.findByEmail(email);
        request.setRequestStatus(RequestStatus.ACCEPTED);
        request.setPassword(passwordEncoder.encode(uuid));
        accountRequestRepository.save(request);
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(uuid));
        user.setCreatedAt(LocalDate.now());
        userRepository.save(user);
        sendMail(email, "Your SITPASS Account Request Approval", body);

    }

    @Override
    public void rejectRequest(String email, String reason) throws MessagingException {
        String body;
        AccountRequest request = accountRequestRepository.findByEmail(email);
        if (reason != null && !reason.isEmpty()) {
            body = String.format("Dear user, we regret to inform you that your account request for SITPASS has been declined. The reason for rejection is: %s. If you have any questions or need further assistance, please contact our support team.", reason);
        } else {
            body = "Dear user, we regret to inform you that your account request for SITPASS has been declined. If you have any questions or need further assistance, please contact our support team.";
        }


        request.setRequestStatus(RequestStatus.REJECTED);
        request.setRejectionReason(reason);
        accountRequestRepository.save(request);

        sendMail(email, "SITPASS Account Request Rejection", body);
    }

}
