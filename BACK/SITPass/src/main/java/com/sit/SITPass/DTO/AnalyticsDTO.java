package com.sit.SITPass.DTO;

import java.time.LocalDate;

public class AnalyticsDTO {
    private Long facilityId;
    private LocalDate from;
    private LocalDate to;

    public AnalyticsDTO() {
    }

    public AnalyticsDTO(Long facilityId, LocalDate from, LocalDate to) {
        this.facilityId = facilityId;
        this.from = from;
        this.to = to;
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }
}
