package com.tu.timeorganizerbe.models;

import com.tu.timeorganizerbe.entities.Activity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PlanGenerationModel {
    private List<Activity> activities = new ArrayList<>();
    private Integer userId;
    private LocalDateTime start;
    private LocalDateTime end;

    public PlanGenerationModel() {
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
}
