package com.tu.timeorganizerbe.repositories;

import com.tu.timeorganizerbe.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}