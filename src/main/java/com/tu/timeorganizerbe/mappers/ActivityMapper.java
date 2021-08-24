package com.tu.timeorganizerbe.mappers;

import com.tu.timeorganizerbe.entities.Activity;
import com.tu.timeorganizerbe.models.ActivityModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    @Mapping(target="user.id", source="userId")
    @Mapping(target="color.secondaryColor", source="secondaryColor")
    Activity map(ActivityModel activityModel);

    void update(ActivityModel activityModel, @MappingTarget Activity activity);
}
