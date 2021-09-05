package com.tu.timeorganizerbe.controllers;

import com.tu.timeorganizerbe.entities.Activity;
import com.tu.timeorganizerbe.models.ActivityModel;
import com.tu.timeorganizerbe.services.ActivityService;
import com.tu.timeorganizerbe.services.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/activities")
public class ActivityController {
    private final ActivityService activityService;
    private final AuthorizationService authorizationService;

    @Autowired
    public ActivityController(ActivityService activityService, AuthorizationService authorizationService) {
        this.activityService = activityService;
        this.authorizationService = authorizationService;
    }

    @GetMapping
    public ResponseEntity<List<Activity>> getUserActivities(@RequestParam(name = "userId") Integer userId,
                                                            Principal userJwt,
                                                            @AuthenticationPrincipal OidcUser user) {
        if (this.authorizationService.isUsersResource(userJwt, user, userId)) {
            List<Activity> activities = this.activityService.findUserActivities(userId);
            return new ResponseEntity<>(activities, HttpStatus.OK);
       }
       throw new AuthorizationServiceException("You have no access to this resource");
    }

    @PostMapping
    public ResponseEntity<Activity> createActivity(@RequestBody ActivityModel activityModel,
                                                   Principal userJwt,
                                                   @AuthenticationPrincipal OidcUser user) {
        if (this.authorizationService.isUsersResource(userJwt, user, activityModel.getUserId())) {
            Activity activity = this.activityService.createActivity(activityModel);
            return new ResponseEntity<>(activity, HttpStatus.CREATED);
        }
        throw new AuthorizationServiceException("You have no access to this resource");
    }

    @PutMapping
    public ResponseEntity<Activity> updateActivity(@RequestBody ActivityModel activityModel,
                                                   Principal userJwt,
                                                   @AuthenticationPrincipal OidcUser user) {
        if (this.authorizationService.isUsersResource(userJwt, user, activityModel.getUserId())) {
            Activity activity = this.activityService.updateActivity(activityModel);
            return new ResponseEntity<>(activity, HttpStatus.OK);
        }
        throw new AuthorizationServiceException("You have no access to this resource");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteActivity(@PathVariable(value="id") Integer id) {
        this.activityService.deleteActivity(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
