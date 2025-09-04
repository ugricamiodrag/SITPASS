package com.sit.SITPass.DTO;

import com.sit.SITPass.model.User;
import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import java.time.LocalDate;

public class UserProfileDTO {

    @Email
    private String email;

    @Nullable
    private String name;

    @Nullable
    private String surname;

    @Nullable
    private String phoneNumber;

    @Nullable
    private LocalDate birthday;

    @Nullable
    private String address;

    @Nullable
    private String city;

    @Nullable
    private String zipCode;

    @Nullable
    private MultipartFile multipartFile;

    @Nullable
    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(@Nullable MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public UserProfileDTO() {}

    public UserProfileDTO(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.phoneNumber = user.getPhoneNumber();
        this.birthday = user.getBirthday();
        this.address = user.getAddress();
        this.city = user.getCity();
        this.zipCode = user.getZipCode();

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getSurname() {
        return surname;
    }

    public void setSurname(@Nullable String surname) {
        this.surname = surname;
    }

    @Nullable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@Nullable String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Nullable
    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(@Nullable LocalDate birthday) {
        this.birthday = birthday;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    public void setAddress(@Nullable String address) {
        this.address = address;
    }

    @Nullable
    public String getCity() {
        return city;
    }

    public void setCity(@Nullable String city) {
        this.city = city;
    }

    @Nullable
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(@Nullable String zipCode) {
        this.zipCode = zipCode;
    }
}
