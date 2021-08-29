package com.tu.timeorganizerbe.services;

import com.tu.timeorganizerbe.entities.ActivityColor;
import com.tu.timeorganizerbe.entities.ActivityInstance;
import com.tu.timeorganizerbe.mappers.ActivityInstanceMapper;
import com.tu.timeorganizerbe.models.ActivityInstanceModel;
import com.tu.timeorganizerbe.repositories.ActivityColorRepository;
import com.tu.timeorganizerbe.repositories.ActivityInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ActivityInstanceService {
    private final ActivityInstanceRepository activityInstanceRepo;
    private final ActivityColorRepository colorRepo;
    private final ActivityInstanceMapper activityInstanceMapper;

    @Autowired
    public ActivityInstanceService(ActivityInstanceRepository activityInstanceRepo,
                                   ActivityColorRepository colorRepo, ActivityInstanceMapper activityInstanceMapper) {
        this.activityInstanceRepo = activityInstanceRepo;
        this.colorRepo = colorRepo;
        this.activityInstanceMapper = activityInstanceMapper;
    }

    public List<ActivityInstance> findUserActivityInstances(Integer userId) {
        return this.activityInstanceRepo.findAllByUserId(userId);
    }

    public ActivityInstance createActivityInstance(ActivityInstanceModel activityInstanceModel) {
        ActivityInstance activityInstance = this.activityInstanceMapper.map(activityInstanceModel);
        ActivityColor color = this.colorRepo.findById(activityInstanceModel.getSecondaryColor()).orElseThrow();
        activityInstance.setColor(color);
        return this.activityInstanceRepo.save(activityInstance);
    }

    public ActivityInstance updateActivityInstance(ActivityInstanceModel activityInstanceModel) {
        ActivityInstance activityInstance = this.activityInstanceRepo.findById(activityInstanceModel.getId())
                .orElseThrow();
        ActivityColor color = this.colorRepo.findById(activityInstanceModel.getSecondaryColor()).orElseThrow();
        this.activityInstanceMapper.update(activityInstanceModel, activityInstance);
        activityInstance.setColor(color);

        return this.activityInstanceRepo.save(activityInstance);
    }

    public void deleteActivityInstance(Integer id) {
        this.activityInstanceRepo.delete(this.activityInstanceRepo.findById(id).orElseThrow());
    }

    public List<ActivityInstance> findInstancesBetweenDates(LocalDateTime start, LocalDateTime end, Integer userId) {
        List<ActivityInstance> startBetween = this.activityInstanceRepo.findAllByUserIdAndStartBetween(userId, start, end);
        List<ActivityInstance> endBetween = this.activityInstanceRepo.findAllByUserIdAndEndBetween(userId, start, end);
        return Stream.concat(startBetween.stream(), endBetween.stream())
                .collect(Collectors.toList());
    }

    public List<ActivityInstance> findMorningInstances(LocalDate date, Integer userId) {
        LocalDateTime start = LocalDateTime.of(date, LocalTime.of(5, 0));
        LocalDateTime end = LocalDateTime.of(date, LocalTime.of(11, 59));
        List<ActivityInstance> startBetween = this.activityInstanceRepo.findAllByUserIdAndStartBetween(userId, start, end);
        List<ActivityInstance> endBetween = this.activityInstanceRepo.findAllByUserIdAndEndBetween(userId, start, end);
        return Stream.concat(startBetween.stream(), endBetween.stream())
                .collect(Collectors.toList());
    }

    public List<ActivityInstance> findAfternoonInstances(LocalDate date, Integer userId) {
        LocalDateTime start = LocalDateTime.of(date, LocalTime.of(12, 0));
        LocalDateTime end = LocalDateTime.of(date, LocalTime.of(17, 0));
        List<ActivityInstance> startBetween = this.activityInstanceRepo.findAllByUserIdAndStartBetween(userId, start, end);
        List<ActivityInstance> endBetween = this.activityInstanceRepo.findAllByUserIdAndEndBetween(userId, start, end);
        return Stream.concat(startBetween.stream(), endBetween.stream())
                .collect(Collectors.toList());
    }

    public List<ActivityInstance> findEveningInstances(LocalDate date, Integer userId) {
        LocalDateTime start = LocalDateTime.of(date, LocalTime.of(17, 1));
        LocalDateTime end = LocalDateTime.of(date, LocalTime.of(4, 59));
        List<ActivityInstance> startBetween = this.activityInstanceRepo.findAllByUserIdAndStartBetween(userId, start, end);
        List<ActivityInstance> endBetween = this.activityInstanceRepo.findAllByUserIdAndEndBetween(userId, start, end);
        return Stream.concat(startBetween.stream(), endBetween.stream())
                .collect(Collectors.toList());
    }

    public List<ActivityInstance> findWholeDayInstances(LocalDate date, Integer userId) {
        LocalDateTime start = LocalDateTime.of(date, LocalTime.of(0, 0));
        LocalDateTime end = LocalDateTime.of(date, LocalTime.of(23, 59));
        List<ActivityInstance> startBetween = this.activityInstanceRepo.findAllByUserIdAndStartBetween(userId, start, end);
        List<ActivityInstance> endBetween = this.activityInstanceRepo.findAllByUserIdAndEndBetween(userId, start, end);
        return Stream.concat(startBetween.stream(), endBetween.stream())
                .collect(Collectors.toList());
    }
}
