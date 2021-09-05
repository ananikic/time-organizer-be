package com.tu.timeorganizerbe.controllers;

import com.tu.timeorganizerbe.entities.User;
import com.tu.timeorganizerbe.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {
        User user = this.userService.findUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User userModel) {
        return new ResponseEntity<>(this.userService.addUser(userModel), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value="id") Integer id,
                                           @RequestBody User userModel) {
        User user = this.userService.updateUser(id, userModel);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
