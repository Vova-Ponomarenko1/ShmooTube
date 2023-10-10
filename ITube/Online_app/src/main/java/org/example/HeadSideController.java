package org.example;

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

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/ITube")
public class HeadSideController {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;

    @GetMapping("/headSide")
    public ModelAndView viewHeadSide(Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView("HeadSide");
        List<Thumbnail> getAllThumbnailsWithId = videoService.updateThumbnailsWithBase64DataUri();
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
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userService.getUserIdByUsername(userDetails.getUsername());
        String avatar = userService.getUserAvatarById(userId);

        if (avatar != null) {
            return ResponseEntity.ok(avatar);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
