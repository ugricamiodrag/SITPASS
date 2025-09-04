package com.sit.SITPass.DTO;

public class RateDTO {

    private Integer equipment;
    private Integer staff;
    private Integer hygiene;
    private Integer space;

    public RateDTO() {
    }

    public RateDTO(Integer equipment, Integer staff, Integer hygiene, Integer space) {
        this.equipment = equipment;
        this.staff = staff;
        this.hygiene = hygiene;
        this.space = space;
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
}
