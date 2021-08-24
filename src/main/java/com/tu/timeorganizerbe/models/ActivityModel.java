package com.tu.timeorganizerbe.models;

import java.util.ArrayList;
import java.util.List;

public class ActivityModel {
    private Integer id;
    private String name;
    private Integer userId;
    private String icon;
    private String secondaryColor;
    private int duration;
    private int frequency;
    private List<String> dayPreference = new ArrayList<>();
    private List<String> timePreference = new ArrayList<>();
    private int concreteTimeHour;
    private int concreteTimeMinute;

    public ActivityModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
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

    public int getConcreteTimeHour() {
        return concreteTimeHour;
    }

    public void setConcreteTimeHour(int concreteTimeHour) {
        this.concreteTimeHour = concreteTimeHour;
    }

    public int getConcreteTimeMinute() {
        return concreteTimeMinute;
    }

    public void setConcreteTimeMinute(int concreteTimeMinute) {
        this.concreteTimeMinute = concreteTimeMinute;
    }
}
