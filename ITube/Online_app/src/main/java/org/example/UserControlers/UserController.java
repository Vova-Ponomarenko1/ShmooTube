package org.example.UserControlers;

import org.example.Users.User;
import org.example.Users.UserRepository;
import org.example.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
@RequestMapping("/ITube")
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

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
        String userAvatar = userService.getUserAvatarById(userService.getUserIdByUsername(userDetails.getUsername()));
        ModelAndView modelAndView = new ModelAndView("UserProfile");
        modelAndView.addObject("hideRegisterButton", true);
        modelAndView.addObject("userAvatar", userAvatar);
        modelAndView.addObject("userName", userDetails.getUsername());

        return modelAndView;
    }
}
