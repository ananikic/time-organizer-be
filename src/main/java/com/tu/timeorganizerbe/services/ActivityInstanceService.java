package com.tu.timeorganizerbe.services;

import com.tu.timeorganizerbe.entities.ActivityColor;
import com.tu.timeorganizerbe.entities.ActivityInstance;
import com.tu.timeorganizerbe.entities.User;
import com.tu.timeorganizerbe.mappers.ActivityInstanceMapper;
import com.tu.timeorganizerbe.models.ActivityInstanceModel;
import com.tu.timeorganizerbe.repositories.ActivityColorRepository;
import com.tu.timeorganizerbe.repositories.ActivityInstanceRepository;
import com.tu.timeorganizerbe.repositories.UserRepository;
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
    private final UserRepository userRepo;
    private final ActivityInstanceMapper activityInstanceMapper;

    @Autowired
    public ActivityInstanceService(ActivityInstanceRepository activityInstanceRepo,
                                   ActivityColorRepository colorRepo, UserRepository userRepo, ActivityInstanceMapper activityInstanceMapper) {
        this.activityInstanceRepo = activityInstanceRepo;
        this.colorRepo = colorRepo;
        this.userRepo = userRepo;
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
        int morningStartHour = 5;
        User user = this.userRepo.findById(userId).orElseThrow();
        if (user.getDayStartHour() > morningStartHour) {
            morningStartHour = user.getDayStartHour();
        }

        LocalDateTime start = LocalDateTime.of(date, LocalTime.of(morningStartHour, 0));
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
        int eveningEndHour = 4;
        User user = this.userRepo.findById(userId).orElseThrow();
        LocalDateTime end = LocalDateTime.of(date.plusDays(1), LocalTime.of(eveningEndHour, 59));
        if (!user.getDayEndHour().equals(24)) {
            eveningEndHour = user.getDayEndHour();
            end = LocalDateTime.of(date, LocalTime.of(eveningEndHour, 59));
        }
        LocalDateTime start = LocalDateTime.of(date, LocalTime.of(17, 1));

        List<ActivityInstance> startBetween = this.activityInstanceRepo.findAllByUserIdAndStartBetween(userId, start, end);
        List<ActivityInstance> endBetween = this.activityInstanceRepo.findAllByUserIdAndEndBetween(userId, start, end);
        return Stream.concat(startBetween.stream(), endBetween.stream())
                .collect(Collectors.toList());
    }

    public List<ActivityInstance> findWholeDayInstances(LocalDate date, Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow();
        int startHour = user.getDayStartHour();
        int endHour = user.getDayEndHour() - 1;

        LocalDateTime start = LocalDateTime.of(date, LocalTime.of(startHour, 0));
        LocalDateTime end = LocalDateTime.of(date, LocalTime.of(endHour, 59));
        List<ActivityInstance> startBetween = this.activityInstanceRepo.findAllByUserIdAndStartBetween(userId, start, end);
        List<ActivityInstance> endBetween = this.activityInstanceRepo.findAllByUserIdAndEndBetween(userId, start, end);
        return Stream.concat(startBetween.stream(), endBetween.stream())
                .collect(Collectors.toList());
    }
}
