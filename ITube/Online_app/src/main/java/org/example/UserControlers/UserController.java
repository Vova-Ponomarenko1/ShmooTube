package org.example.UserControlers;

import org.example.Users.User;
import org.example.Users.UserService;
import org.example.Video.Thumbnail;
import org.example.Video.VideoRepository;
import org.example.Video.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/ITube")
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private VideoService videoService;

    @GetMapping("/new-user")
    public ModelAndView viewRegisterPage() {
        return new ModelAndView("registrationNewUsers");
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserDto userDto) throws IOException {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setAvatar(userDto.getAvatar());
        user.setRole("USER");

        userService.newUser(user);

        return "Користувач успішно зареєстрований.";
    }

    @GetMapping("/my/profile")
    public ModelAndView viewUserProfile(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        long userId = userService.getUserIdByUsername(userDetails.getUsername());
        String userAvatar = userService.getUserAvatarById(userId);
        List<Thumbnail> videoThumbnail = videoService.updateThumbnailsWithBase64DataUri(
            videoRepository.findVideoThumbnailByUserId(userId));


        ModelAndView modelAndView = new ModelAndView("UserProfile");
        modelAndView.addObject("videoList", videoThumbnail);
        modelAndView.addObject("hideRegisterButton", true);
        modelAndView.addObject("userAvatar", userAvatar);
        modelAndView.addObject("userName", userDetails.getUsername());
        modelAndView.addObject("userId", userId);

        return modelAndView;
    }

    @GetMapping("/loadMore")
    public List<Thumbnail> loadMoreVideos(@RequestParam("lastVideoId") Long lastVideoId,
                                          @RequestParam("userID") Long userID) {
        return videoService.updateThumbnailsWithBase64DataUri(
            videoRepository.findMoreVideosByLastVideoIdAndUserId(lastVideoId, userID));
    }
}
