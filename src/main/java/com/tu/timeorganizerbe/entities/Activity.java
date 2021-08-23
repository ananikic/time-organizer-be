package com.tu.timeorganizerbe.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties({"activities", "activityInstances"})
    private User user;
    @Column(nullable = false)
    private String icon;
    @ManyToOne(targetEntity = ActivityColor.class)
    @JoinColumn(name = "color", referencedColumnName = "secondaryColor", nullable = false)
    private ActivityColor color;
    @Column(nullable = false)
    private int duration;
    @Column(nullable = false)
    private int frequency;
    @ElementCollection
    private List<String> dayPreference = new ArrayList<>();
    @ElementCollection
    private List<String> timePreference = new ArrayList<>();
    private LocalTime concreteTime;

    public Activity() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ActivityColor getColor() {
        return color;
    }

    public void setColor(ActivityColor color) {
        this.color = color;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public List<String> getDayPreference() {
        return dayPreference;
    }

    public void setDayPreference(List<String> dayPreference) {
        this.dayPreference = dayPreference;
    }

    public List<String> getTimePreference() {
        return timePreference;
    }

    public void setTimePreference(List<String> timePreference) {
        this.timePreference = timePreference;
    }

    public LocalTime getConcreteTime() {
        return concreteTime;
    }

    public void setConcreteTime(LocalTime concreteTime) {
        this.concreteTime = concreteTime;
    }
}
