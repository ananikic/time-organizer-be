package com.tu.timeorganizerbe.services;

import com.tu.timeorganizerbe.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class AuthorizationService {
    private final UserService userService;

    @Autowired
    public AuthorizationService(UserService userService) {
        this.userService = userService;
    }

    public boolean isUsersResource(Principal userJwt, OidcUser user, Integer resourceUserId) {
        User currentUser;
        if (user != null) {
            currentUser = this.userService.findUserByEmail(user.getEmail());
        } else {
            currentUser = this.userService.findUserByEmail(userJwt.getName());
        }

        return currentUser.getId().equals(resourceUserId);
    }
}
