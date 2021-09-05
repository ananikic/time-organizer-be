package com.tu.timeorganizerbe.entities;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique=true)
    private String email;
    @Column(nullable = false)
    private String username;
    private Integer dayStartHour;
    private Integer dayEndHour;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getDayStartHour() {
        return dayStartHour;
    }

    public void setDayStartHour(Integer dayStartHour) {
        this.dayStartHour = dayStartHour;
    }

    public Integer getDayEndHour() {
        return dayEndHour;
    }

    public void setDayEndHour(Integer dayEndHour) {
        this.dayEndHour = dayEndHour;
    }
}
