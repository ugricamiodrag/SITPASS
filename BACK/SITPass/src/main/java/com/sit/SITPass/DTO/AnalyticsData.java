package com.sit.SITPass.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AnalyticsData {
    private LocalDateTime date;
    private Long usersCount;
    private Long reviewsCount;

    public AnalyticsData() {
    }

    public AnalyticsData(LocalDateTime date, Long usersCount, Long reviewsCount) {
        this.date = date;
        this.usersCount = usersCount;
        this.reviewsCount = reviewsCount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(Long usersCount) {
        this.usersCount = usersCount;
    }

    public Long getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Long reviewsCount) {
        this.reviewsCount = reviewsCount;
    }
}
