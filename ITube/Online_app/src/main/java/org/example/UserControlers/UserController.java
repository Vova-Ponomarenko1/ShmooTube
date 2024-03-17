package org.example.UserControlers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.Users.User;
import org.example.Users.UserService;
import org.example.Video.Thumbnail;
import org.example.Video.VideoRepository;
import org.example.Video.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/ITube")
public class UserController {

    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private VideoRepository videoRepository;
    private VideoService videoService;

    @Autowired
    public UserController(PasswordEncoder passwordEncoder,
                          UserService userService, VideoRepository videoRepository,
                          VideoService videoService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.videoRepository = videoRepository;
        this.videoService = videoService;
    }

    @GetMapping("/new-user")
    public ModelAndView viewRegisterPage() {
        return new ModelAndView("registrationNewUsers");
    }

    @GetMapping("/googleLogin")
    public RedirectView test() {
        return new RedirectView("/oauth2/authorization/google");
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
    @GetMapping("/1")
    public String GoogleCallback(Authentication authentication) {
        DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();

        System.out.println((String) userDetails.getAttribute("sub"));

        return "redirect:/user";
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
