package com.sit.SITPass.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rates")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "equipment")
    private Integer equipment;

    @Column(nullable = false, name = "staff")
    private Integer staff;

    @Column(nullable = false, name = "hygene")
    private Integer hygene;

    @Column(nullable = false, name = "space")
    private Integer space;

    public Rate() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getHygene() {
        return hygene;
    }

    public void setHygene(Integer hygene) {
        this.hygene = hygene;
    }

    public Integer getSpace() {
        return space;
    }

    public void setSpace(Integer space) {
        this.space = space;
    }
}
