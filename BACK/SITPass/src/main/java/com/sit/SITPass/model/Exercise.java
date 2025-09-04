package com.sit.SITPass.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "exercise")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "fromDate", nullable = false)
    private LocalDateTime from;

    @Column(name = "untilDate")
    private LocalDateTime until;

    @ManyToOne(fetch = FetchType.EAGER) // Change OneToOne to ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    public Exercise() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getUntil() {
        return until;
    }

    public void setUntil(LocalDateTime until) {
        this.until = until;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(id, exercise.id) && Objects.equals(user, exercise.user) && Objects.equals(from, exercise.from) && Objects.equals(until, exercise.until);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, from, until);
    }
}
