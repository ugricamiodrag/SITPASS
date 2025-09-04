package com.sit.SITPass.DTO;

import javax.validation.constraints.Email;

public class EmailDTO {
    @Email
    private String email;

    public EmailDTO() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

