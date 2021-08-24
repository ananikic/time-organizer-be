package com.tu.timeorganizerbe.services;

import com.tu.timeorganizerbe.entities.Activity;
import com.tu.timeorganizerbe.entities.ActivityColor;
import com.tu.timeorganizerbe.mappers.ActivityMapper;
import com.tu.timeorganizerbe.models.ActivityModel;
import com.tu.timeorganizerbe.repositories.ActivityColorRepository;
import com.tu.timeorganizerbe.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {
    private final ActivityRepository activityRepo;
    private final ActivityColorRepository colorRepo;
    private final ActivityMapper activityMapper;

    @Autowired
    public ActivityService(ActivityRepository activityRepo, ActivityColorRepository colorRepo, ActivityMapper activityMapper) {
        this.activityRepo = activityRepo;
        this.colorRepo = colorRepo;
        this.activityMapper = activityMapper;
    }

    public List<Activity> findUserActivities(Integer userId) {
        return this.activityRepo.findAllByUserId(userId);
    }

    public Activity createActivity(ActivityModel activityModel) {
        Activity activity = this.activityMapper.map(activityModel);
        return this.activityRepo.save(activity);
    }

    public Activity updateActivity(ActivityModel activityModel) {
        Activity activity = this.activityRepo.findById(activityModel.getId()).orElseThrow();
        ActivityColor color = this.colorRepo.findById(activityModel.getSecondaryColor()).orElseThrow();
        this.activityMapper.update(activityModel, activity);
        activity.setColor(color);
        return this.activityRepo.save(activity);
    }
}
