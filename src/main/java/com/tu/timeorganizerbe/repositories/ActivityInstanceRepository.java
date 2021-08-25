package com.tu.timeorganizerbe.repositories;

import com.tu.timeorganizerbe.entities.ActivityInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityInstanceRepository extends JpaRepository<ActivityInstance, Integer> {
    List<ActivityInstance> findAllByUserId(Integer userId);
}
