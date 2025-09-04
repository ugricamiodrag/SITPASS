package com.sit.SITPass.DTO;

import java.util.List;

public class AnalyticsResponseDTO {
    private List<AnalyticsData> analyticsData;
    private List<TimePeriodData> timePeriodData;

    public AnalyticsResponseDTO(List<AnalyticsData> analyticsData, List<TimePeriodData> timePeriodData) {
        this.analyticsData = analyticsData;
        this.timePeriodData = timePeriodData;
    }

    public List<AnalyticsData> getAnalyticsData() {
        return analyticsData;
    }

    public void setAnalyticsData(List<AnalyticsData> analyticsData) {
        this.analyticsData = analyticsData;
    }

    public List<TimePeriodData> getTimePeriodData() {
        return timePeriodData;
    }

    public void setTimePeriodData(List<TimePeriodData> timePeriodData) {
        this.timePeriodData = timePeriodData;
    }
}
