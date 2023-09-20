package org.example.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserValidator userValidator;

    @Autowired UserRepository userRepository;

    @Override
    public void newUser(User user) throws IOException {
        userValidator.validateUser(user);
        MultipartFile imageMultipartFile = user.getAvatar();
        String base64Image = "data:image/png;base64," + Base64.getEncoder().encodeToString(imageMultipartFile.getBytes());
        user.setAvatar_Base64(base64Image);
        userRepository.save(user);
    }

    @Override
    public Long getUserIdByUsername(String username) {
        return userRepository.getUserIdByUsername(username);
    }

    @Override
    public String getUserAvatarById(Long userId) {
        return userRepository.getUserAvatarById(userId);
    }
}
