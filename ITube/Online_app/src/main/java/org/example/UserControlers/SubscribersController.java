package org.example.UserControlers;

import org.example.LikeAndSubscriber.SubscribersRepository;
import org.example.Security.AuthenticationHandler;
import org.example.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ITube")
public class SubscribersController {

    private SubscribersRepository subscribersRepository;
    private UserService userService;
    private AuthenticationHandler authenticationHandler;
    @Autowired
    public SubscribersController(SubscribersRepository subscribersRepository, UserService userService,
                                 AuthenticationHandler authenticationHandler) {
        this.subscribersRepository = subscribersRepository;
        this.userService = userService;
        this.authenticationHandler = authenticationHandler;
    }





    @PostMapping("/subscriber/{userId}")
    public void subscribe(@PathVariable Long userId,
                          Authentication authentication) {
        Long subscriberId = authenticationHandler.getUserIdFromAuthentication(authentication);
        subscribersRepository.subscribeOrUnsubscribe(userId, subscriberId);
    }

}
