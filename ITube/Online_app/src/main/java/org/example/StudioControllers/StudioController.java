package org.example.StudioControllers;

import org.example.Security.AuthenticationHandler;
import org.example.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class StudioController {

    private UserService userService;
    private AuthenticationHandler authenticationHandler;
    @Autowired
    public StudioController(UserService userService,
                            AuthenticationHandler authenticationHandler) {
        this.userService = userService;
        this.authenticationHandler = authenticationHandler;
    }

    @GetMapping("/my/studio")
    public ModelAndView viewStudioPage(Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView("Studio");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        long userId = authenticationHandler.getUserIdFromAuthentication(authentication);
        String userAvatar = userService.getUserAvatarById(userId);

        modelAndView.addObject("userAvatar", userAvatar);
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("userName", userDetails.getUsername());

        return modelAndView;
    }
}
