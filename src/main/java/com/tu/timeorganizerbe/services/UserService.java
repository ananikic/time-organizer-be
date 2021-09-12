package com.tu.timeorganizerbe.services;

import com.tu.timeorganizerbe.entities.User;
import com.tu.timeorganizerbe.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User findUser(Integer id) {
        return this.userRepo.findById(id).orElseThrow();
    }

    public User findUserByEmail(String email) {
        return this.userRepo.findByEmail(email);
    }

    public User addUser(User userModel) {
        return this.userRepo.save(userModel);
    }

    public User updateUser(Integer id, User userModel) {
        User user = this.userRepo.findById(id).orElseThrow();
        user.setDayStartHour(userModel.getDayStartHour());
        user.setDayEndHour(userModel.getDayEndHour());
        return this.userRepo.save(user);
    }

}
