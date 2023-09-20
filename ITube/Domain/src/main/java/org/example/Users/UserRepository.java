package org.example.Users;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

    void save(User user);

    User findById(long user_id);

    Long getUserIdByUsername(String username);

    String getUserAvatarById(Long userId);
}
