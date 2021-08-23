package com.tu.timeorganizerbe.repositories;

import com.tu.timeorganizerbe.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
}
