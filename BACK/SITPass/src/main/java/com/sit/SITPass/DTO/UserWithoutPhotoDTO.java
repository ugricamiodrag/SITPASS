package com.sit.SITPass.DTO;


import com.sit.SITPass.model.User;

public class UserWithoutPhotoDTO {

    private Long id;
    private String email;

    public UserWithoutPhotoDTO(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public UserWithoutPhotoDTO() {
    }

    public UserWithoutPhotoDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
