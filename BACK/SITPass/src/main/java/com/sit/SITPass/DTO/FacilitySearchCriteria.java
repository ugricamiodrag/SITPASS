package com.sit.SITPass.DTO;

import jakarta.annotation.Nullable;

import java.util.List;

public class FacilitySearchCriteria {
    @Nullable
    private List<String> cities;
    @Nullable
    private List<String> disciplines;

    @Nullable
    private Double minGrade;
    @Nullable
    private Double maxGrade;

    @Nullable
    private List<WorkDayDTO> workDaySearchCriteriaList;

    private boolean isActive = true;

    public FacilitySearchCriteria() {
    }

    public FacilitySearchCriteria(List<String> cities, List<String> disciplines, Double minGrade, Double maxGrade, List<WorkDayDTO> workDaySearchCriteriaList) {
        this.cities = cities;
        this.disciplines = disciplines;
        this.minGrade = minGrade;
        this.maxGrade = maxGrade;
        this.workDaySearchCriteriaList = workDaySearchCriteriaList;
    }

    public FacilitySearchCriteria(List<String> cities, List<String> disciplines, Double minGrade, Double maxGrade, List<WorkDayDTO> workDaySearchCriteriaList, boolean isActive) {
        this.cities = cities;
        this.disciplines = disciplines;
        this.minGrade = minGrade;
        this.maxGrade = maxGrade;
        this.workDaySearchCriteriaList = workDaySearchCriteriaList;
        this.isActive = isActive;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public List<String> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(List<String> disciplines) {
        this.disciplines = disciplines;
    }

    public Double getMinGrade() {
        return minGrade;
    }

    public void setMinGrade(Double minGrade) {
        this.minGrade = minGrade;
    }

    public Double getMaxGrade() {
        return maxGrade;
    }

    public void setMaxGrade(Double maxGrade) {
        this.maxGrade = maxGrade;
    }

    public List<WorkDayDTO> getWorkDaySearchCriteriaList() {
        return workDaySearchCriteriaList;
    }

    public void setWorkDaySearchCriteriaList(List<WorkDayDTO> workDaySearchCriteriaList) {
        this.workDaySearchCriteriaList = workDaySearchCriteriaList;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
