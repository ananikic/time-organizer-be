package com.tu.timeorganizerbe.repositories;

import com.tu.timeorganizerbe.entities.ActivityInstance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityInstanceRepository extends JpaRepository<ActivityInstance, Integer> {
}
