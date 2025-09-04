package com.sit.SITPass.DTO;

import com.sit.SITPass.model.User;

import javax.validation.constraints.Email;

public class UserToManageDTO {
    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserToManageDTO(String email) {
        this.email = email;
    }

    public UserToManageDTO() {
    }

    public UserToManageDTO(User user) {
        this.email = user.getEmail();
    }


}
