package org.example.UserControlers;

import org.example.Users.User;
import org.example.Users.UserRepository;
import org.example.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

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
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setAvatar(userDto.getAvatar());
        user.setRole("USER");

        userService.newUser(user);


        return "Користувач успішно зареєстрований.";
    }

}
