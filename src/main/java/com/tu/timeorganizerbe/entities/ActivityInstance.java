package com.tu.timeorganizerbe.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ActivityInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String title;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties({"activities", "activityInstances"})
    private User user;
    private LocalDateTime start;
    private LocalDateTime end;
    @ManyToOne(targetEntity = ActivityColor.class)
    @JoinColumn(name = "color", referencedColumnName = "secondaryColor", nullable = false)
    private ActivityColor color;

    public ActivityInstance() {
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public ActivityColor getColor() {
        return color;
    }

    public void setColor(ActivityColor color) {
        this.color = color;
    }
}
