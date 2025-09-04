package com.sit.SITPass.DTO;

import javax.validation.constraints.Email;

import com.sit.SITPass.model.AccountRequest;
import com.sit.SITPass.model.RequestStatus;

import javax.validation.constraints.*;

import java.time.LocalDate;


public class UserRequestDTO {

    private Long id;

    @Email
    private String email;

    private String address;

    private LocalDate createdDate = LocalDate.now();

    private RequestStatus status;


    public UserRequestDTO() {
    }

    public UserRequestDTO(AccountRequest accountRequest) {
        this.id = accountRequest.getId();
        this.email = accountRequest.getEmail();
        this.address = accountRequest.getAddress();
        this.createdDate = LocalDate.now();
        this.status = accountRequest.getRequestStatus();
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
