package com.sit.SITPass.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TimePeriodData {
    private LocalDateTime period;
    private Long usersCount;

    public TimePeriodData() {
    }

    public TimePeriodData(LocalDateTime period, Long usersCount) {
        this.period = period;
        this.usersCount = usersCount;
    }

    public LocalDateTime getPeriod() {
        return period;
    }

    public void setPeriod(LocalDateTime period) {
        this.period = period;
    }

    public Long getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(Long usersCount) {
        this.usersCount = usersCount;
    }
}
