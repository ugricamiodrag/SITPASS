package com.sit.SITPass.DTO;

public class ExerciseDTO {
    private Long id;
    private Long userId;

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

    public ExerciseDTO(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
    }

    public ExerciseDTO() {
    }
}
