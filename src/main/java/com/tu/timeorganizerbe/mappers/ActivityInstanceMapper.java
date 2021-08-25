package com.tu.timeorganizerbe.mappers;

import com.tu.timeorganizerbe.entities.ActivityInstance;
import com.tu.timeorganizerbe.models.ActivityInstanceModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ActivityInstanceMapper {
    @Mapping(target="user.id", source="userId")
    @Mapping(target="color.secondaryColor", source="secondaryColor")
    ActivityInstance map(ActivityInstanceModel activityInstanceModel);

    void update(ActivityInstanceModel activityInstanceModel, @MappingTarget ActivityInstance activityInstance);
}
