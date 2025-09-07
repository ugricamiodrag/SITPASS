package com.sit.SITPass.model;

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

    @OneToOne(mappedBy = "descriptionFile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private User user;



    @OneToOne(mappedBy = "descriptionFile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Facility facility;



    public DescriptionFile() {
    }
}
