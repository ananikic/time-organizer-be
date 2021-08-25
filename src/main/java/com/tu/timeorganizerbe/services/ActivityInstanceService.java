package com.tu.timeorganizerbe.services;

import com.tu.timeorganizerbe.entities.ActivityColor;
import com.tu.timeorganizerbe.entities.ActivityInstance;
import com.tu.timeorganizerbe.mappers.ActivityInstanceMapper;
import com.tu.timeorganizerbe.models.ActivityInstanceModel;
import com.tu.timeorganizerbe.repositories.ActivityColorRepository;
import com.tu.timeorganizerbe.repositories.ActivityInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
