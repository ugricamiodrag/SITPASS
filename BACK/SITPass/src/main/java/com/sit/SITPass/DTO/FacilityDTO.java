package com.sit.SITPass.DTO;

import com.sit.SITPass.model.Discipline;
import com.sit.SITPass.model.Facility;
import com.sit.SITPass.model.Image;
import com.sit.SITPass.model.WorkDay;

import java.util.HashSet;
import java.util.Set;

public class FacilityDTO {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String city;
    private double totalRating;
    private boolean isActive;
    private Set<WorkDay> workDays;
    private Set<Discipline> disciplines;
    private Set<String> images;





    public FacilityDTO() {
    }

    public FacilityDTO(Facility facility) {
        Set<String> set = new HashSet<String>();
        for (Image image : facility.getImages()) {
            set.add(image.getPath());
        }
        this.id = facility.getId();
        this.name = facility.getName();
        this.description = facility.getDescription();
        this.address = facility.getAddress();
        this.city = facility.getCity();
        this.totalRating = facility.getTotalRating();
        this.isActive = facility.isActive();
        this.workDays = facility.getWorkDays();
        this.disciplines = facility.getDisciplines();
        this.images = set;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Set<WorkDay> getWorkDays() {
        return workDays;
    }

    public void setWorkDays(Set<WorkDay> workDays) {
        this.workDays = workDays;
    }

    public Set<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(Set<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    public Set<String> getImages() {
        return images;
    }

    public void setImages(Set<String> images) {
        this.images = images;
    }

    public double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(double totalRating) {
        this.totalRating = totalRating;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
