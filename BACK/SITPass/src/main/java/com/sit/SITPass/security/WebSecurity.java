package com.sit.SITPass.security;

import com.sit.SITPass.model.User;
import com.sit.SITPass.service.AccountRequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class WebSecurity {

    @Autowired
    private AccountRequestService requestService;

    public boolean checkEmail(HttpServletRequest request, String email) {
        boolean result = requestService.emailExists(email);
        return result;
    }
}