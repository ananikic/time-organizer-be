package com.tu.timeorganizerbe.controllers;

import com.tu.timeorganizerbe.entities.ActivityInstance;
import com.tu.timeorganizerbe.models.ActivityInstanceModel;
import com.tu.timeorganizerbe.services.ActivityInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity-instances")
public class ActivityInstanceController {
    private final ActivityInstanceService activityInstanceService;

    @Autowired
    public ActivityInstanceController(ActivityInstanceService activityInstanceService) {
        this.activityInstanceService = activityInstanceService;
    }

    @GetMapping
    public ResponseEntity<List<ActivityInstance>> getUserActivityInstances(
            @RequestParam(name = "userId") Integer userId) {
        List<ActivityInstance> activityInstances = this.activityInstanceService.findUserActivityInstances(userId);
        return new ResponseEntity<>(activityInstances, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ActivityInstance> createActivityInstance(@RequestBody ActivityInstanceModel activityInstanceModel) {
        ActivityInstance activityInstance = this.activityInstanceService.createActivityInstance(activityInstanceModel);
        return new ResponseEntity<>(activityInstance, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ActivityInstance> updateActivityInstance(@RequestBody ActivityInstanceModel activityInstanceModel) {
        ActivityInstance activityInstance = this.activityInstanceService.updateActivityInstance(activityInstanceModel);
        return new ResponseEntity<>(activityInstance, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteActivityInstance(@PathVariable(value="id") Integer id) {
        this.activityInstanceService.deleteActivityInstance(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
