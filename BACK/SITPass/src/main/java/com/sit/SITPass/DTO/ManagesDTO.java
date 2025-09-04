package com.sit.SITPass.DTO;

import com.sit.SITPass.model.Manages;

import java.time.LocalDate;

public class ManagesDTO {
    private UserToManageDTO user;
    private LocalDate startDate;
    private Long facilityId;

    public UserToManageDTO getUser() {
        return user;
    }

    public void setUser(UserToManageDTO user) {
        this.user = user;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    public ManagesDTO(UserToManageDTO user, LocalDate startDate, Long facilityId) {
        this.user = user;
        this.startDate = startDate;
        this.facilityId = facilityId;
    }

    public ManagesDTO() {
    }

    public ManagesDTO(Manages manages) {
        this.user = new UserToManageDTO(manages.getUser().getEmail());
        this.startDate = manages.getStartDate();
        this.facilityId = manages.getFacility().getId();
    }
}
