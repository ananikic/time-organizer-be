package com.tu.timeorganizerbe.entities;

import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique=true)
    private String email;
    @Column(nullable = false)
    private String username;
    @OneToMany(mappedBy = "user")
    @SortNatural
    @OrderBy("name ASC")
    private List<Activity> activities = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    @SortNatural
    @OrderBy("title ASC")
    private List<ActivityInstance> activityInstances = new ArrayList<>();
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

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<ActivityInstance> getActivityInstances() {
        return activityInstances;
    }

    public void setActivityInstances(List<ActivityInstance> activityInstances) {
        this.activityInstances = activityInstances;
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
