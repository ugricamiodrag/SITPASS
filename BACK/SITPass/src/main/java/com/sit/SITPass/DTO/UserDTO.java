package com.sit.SITPass.DTO;


import com.sit.SITPass.model.User;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


public class UserDTO {


    @Email
    private String email;

    @NotBlank
    private String password;

    public UserDTO(User createdUser) {

        this.email = createdUser.getEmail();
        this.password = createdUser.getPassword();
    }

    public UserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
