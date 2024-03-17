package org.example.Security;

import org.example.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHandler {
    private UserService userService;
    @Autowired
    public AuthenticationHandler(UserService userService) {
        this.userService = userService;
    }

    public Long getUserIdFromAuthentication(Authentication authentication) {
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String email = (String) oauth2User.getAttribute("email");
            return userService.getUserIdByUserEmail(email);
        } else  {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long subscriberId = userService.getUserIdByUsername(userDetails.getUsername());
            return subscriberId;
        }
    }
}
