package org.example;

import org.example.Security.AuthenticationHandler;
import org.example.Users.UserService;
import org.example.Video.Thumbnail;
import org.example.Video.VideoRepository;
import org.example.Video.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/ITube")
public class HeadSideController {

    private VideoRepository videoRepository;
    private UserService userService;
    private VideoService videoService;
    private AuthenticationHandler authenticationHandler;
    @Autowired
    public HeadSideController(VideoRepository videoRepository, UserService userService,
                              VideoService videoService, AuthenticationHandler authenticationHandler) {
        this.videoRepository = videoRepository;
        this.userService = userService;
        this.videoService = videoService;
        this.authenticationHandler = authenticationHandler;
    }

    @GetMapping("/headSide")
    public ModelAndView viewHeadSide(Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView("HeadSide");
        List<Thumbnail> getAllThumbnailsWithId = videoService.updateThumbnailsWithBase64DataUri(
            videoRepository.getAllThumbnailsWithIds());

        if (authentication != null && authentication.isAuthenticated()) {
            modelAndView.addObject("hideRegisterButton", true);
        } else {
            modelAndView.addObject("hideRegisterButton", false);
        }
        modelAndView.addObject("videoList", getAllThumbnailsWithId);
        return modelAndView;
    }

    @GetMapping("/getUserAvatar")
    public ResponseEntity<String> getUserAvatar(Authentication authentication) {
        Long userId =  authenticationHandler.getUserIdFromAuthentication(authentication);
        String avatar = userService.getUserAvatarById(userId);

        if (avatar != null) {
            return ResponseEntity.ok(avatar);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
