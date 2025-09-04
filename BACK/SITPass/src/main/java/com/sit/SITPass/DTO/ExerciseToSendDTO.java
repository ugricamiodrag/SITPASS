package com.sit.SITPass.DTO;

import java.time.LocalDateTime;

public class ExerciseToSendDTO {
    private Long id;
    private Long userId;
    private LocalDateTime from;
    private LocalDateTime until;
    private Long facilityId;

    public ExerciseToSendDTO(Long id, Long userId, LocalDateTime from, LocalDateTime until, Long facilityId) {
        this.id = id;
        this.userId = userId;
        this.from = from;
        this.until = until;
        this.facilityId = facilityId;
    }

    public ExerciseToSendDTO(Long userId, LocalDateTime from, LocalDateTime until, Long facilityId) {
        this.userId = userId;
        this.from = from;
        this.until = until;
        this.facilityId = facilityId;
    }

    public ExerciseToSendDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getUntil() {
        return until;
    }

    public void setUntil(LocalDateTime until) {
        this.until = until;
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }
}
