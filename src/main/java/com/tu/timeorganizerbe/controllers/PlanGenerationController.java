package com.tu.timeorganizerbe.controllers;

import com.tu.timeorganizerbe.entities.ActivityInstance;
import com.tu.timeorganizerbe.models.PlanGenerationModel;
import com.tu.timeorganizerbe.services.PlanGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plan")
public class PlanGenerationController {
    private final PlanGenerationService planGenerationService;

    @Autowired
    public PlanGenerationController(PlanGenerationService planGenerationService) {
        this.planGenerationService = planGenerationService;
    }

    @PostMapping("/week")
    public ResponseEntity<?> createWeeklyPlan(@RequestBody PlanGenerationModel planGenerationModel) {
        this.planGenerationService.generateWeek(planGenerationModel);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
