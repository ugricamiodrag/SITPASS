package com.sit.SITPass.DTO;

import java.time.LocalDateTime;

public class ReviewPageDTO {
    private Long id;
    private String name;
    private Integer equipment;
    private Integer staff;
    private Integer hygiene;
    private Integer space;
    private CommentForReviewDTO comment;
    private LocalDateTime createdAt;
    private int exerciseCount;
    private boolean hidden;

    public class CommentForReviewDTO {
        private Long id;
        private String comment;

        public CommentForReviewDTO() {
        }

        public CommentForReviewDTO(Long id, String comment) {
            this.id = id;
            this.comment = comment;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }

    public ReviewPageDTO() {
    }

    public ReviewPageDTO(Long id, String name, Integer equipment, Integer staff, Integer hygiene, Integer space, CommentForReviewDTO comment, LocalDateTime createdAt, int exerciseCount, boolean hidden) {
        this.id = id;
        this.name = name;
        this.equipment = equipment;
        this.staff = staff;
        this.hygiene = hygiene;
        this.space = space;
        this.comment = comment;
        this.createdAt = createdAt;
        this.exerciseCount = exerciseCount;
        this.hidden = hidden;
    }

    public ReviewPageDTO(Long id, String name, Integer equipment, Integer staff, Integer hygiene, Integer space, LocalDateTime createdAt, int exerciseCount, boolean hidden) {
        this.id = id;
        this.name = name;
        this.equipment = equipment;
        this.staff = staff;
        this.hygiene = hygiene;
        this.space = space;
        this.createdAt = createdAt;
        this.exerciseCount = exerciseCount;
        this.hidden = hidden;
    }

    public ReviewPageDTO(String name, Integer equipment, Integer staff, Integer hygiene, Integer space, LocalDateTime createdAt, int exerciseCount, boolean hidden) {
        this.name = name;
        this.equipment = equipment;
        this.staff = staff;
        this.hygiene = hygiene;
        this.space = space;
        this.createdAt = createdAt;
        this.exerciseCount = exerciseCount;
        this.hidden = hidden;
    }

    public ReviewPageDTO(String name, Integer equipment, Integer staff, Integer hygiene, Integer space, CommentForReviewDTO comment, LocalDateTime createdAt, int exerciseCount, boolean hidden) {
        this.name = name;
        this.equipment = equipment;
        this.staff = staff;
        this.hygiene = hygiene;
        this.space = space;
        this.comment = comment;
        this.createdAt = createdAt;
        this.exerciseCount = exerciseCount;
        this.hidden = hidden;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEquipment() {
        return equipment;
    }

    public void setEquipment(Integer equipment) {
        this.equipment = equipment;
    }

    public Integer getStaff() {
        return staff;
    }

    public void setStaff(Integer staff) {
        this.staff = staff;
    }

    public Integer getHygiene() {
        return hygiene;
    }

    public void setHygiene(Integer hygiene) {
        this.hygiene = hygiene;
    }

    public Integer getSpace() {
        return space;
    }

    public void setSpace(Integer space) {
        this.space = space;
    }

    public CommentForReviewDTO getComment() {
        return comment;
    }

    public void setComment(CommentForReviewDTO comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getExerciseCount() {
        return exerciseCount;
    }

    public void setExerciseCount(int exerciseCount) {
        this.exerciseCount = exerciseCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
