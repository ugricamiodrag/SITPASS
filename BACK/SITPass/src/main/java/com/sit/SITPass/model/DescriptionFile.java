package com.sit.SITPass.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "descriptionFiles")
public class DescriptionFile {

    @Id
    private String serverFileName;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    @JsonBackReference(value = "facility-description")
    private Facility facility;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-description")
    private User user;




    public DescriptionFile() {
    }
}
