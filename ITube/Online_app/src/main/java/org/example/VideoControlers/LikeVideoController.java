package org.example.VideoControlers;

import org.example.LikeAndSubscriber.LikeRepository;
import org.example.Security.AuthenticationHandler;
import org.example.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ITube")
public class LikeVideoController {
    private UserService userService;

    private LikeRepository likeRepository;
    private AuthenticationHandler authenticationHandler;
    @Autowired
    public LikeVideoController(UserService userService, LikeRepository likeRepository,
                               AuthenticationHandler authenticationHandler) {
        this.userService = userService;
        this.likeRepository = likeRepository;
        this.authenticationHandler = authenticationHandler;
    }




    @PostMapping("/likeVideo")
    public void likeVideo(@RequestBody VideoLike videoLike,
                          Authentication authentication) {
        Long userId =  authenticationHandler.getUserIdFromAuthentication(authentication);
        likeRepository.addLikeAndDeleteLike(videoLike.getVideoId(), userId);
    }


    @PostMapping("/dislikeVideo")
    public void dislikeVideo(@RequestBody VideoLike videoLike,
                          Authentication authentication) {
        Long userId = authenticationHandler.getUserIdFromAuthentication(authentication);
        likeRepository.addDislikeAndDeleteDislike(videoLike.getVideoId(), userId);
    }
}
