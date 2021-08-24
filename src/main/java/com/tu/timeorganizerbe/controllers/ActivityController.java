package com.tu.timeorganizerbe.controllers;

import com.tu.timeorganizerbe.entities.Activity;
import com.tu.timeorganizerbe.models.ActivityModel;
import com.tu.timeorganizerbe.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activities")
public class ActivityController {
    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public ResponseEntity<List<Activity>> getUserActivities(
            @RequestParam(name = "userId") Integer userId) {
        List<Activity> activities = this.activityService.findUserActivities(userId);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Activity> createActivity(@RequestBody ActivityModel activityModel) {
        Activity activity = this.activityService.createActivity(activityModel);
        return new ResponseEntity<>(activity, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Activity> updateActivity(@RequestBody ActivityModel activityModel) {
        Activity activity = this.activityService.updateActivity(activityModel);
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }
}
