package org.example.Users;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface UserService {

    void newUser(User user) throws IOException;

    Long getUserIdByUsername(String username);

    String getUserAvatarById(Long userId);
}
