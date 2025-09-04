package com.sit.SITPass.service;

import com.sit.SITPass.DTO.UserRequestDTO;
import com.sit.SITPass.model.AccountRequest;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;

public interface AccountRequestService {
    boolean emailExists(String email);
    void createRequest(AccountRequest request);
    Page<AccountRequest> getRequests(int page, int size);
    void sendMail(String email, String subject, String body) throws MessagingException;
    void acceptRequest(String email) throws MessagingException;
    void rejectRequest(String email, String reason) throws MessagingException;
}
