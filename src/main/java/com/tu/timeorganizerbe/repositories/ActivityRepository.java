package com.tu.timeorganizerbe.repositories;

import com.tu.timeorganizerbe.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    List<Activity> findAllByUserId(Integer userId);
}
