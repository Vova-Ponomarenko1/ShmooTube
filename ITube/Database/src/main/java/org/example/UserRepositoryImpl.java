package org.example;

import org.example.Users.User;
import org.example.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (username, password, email, avatar_base64, role) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(
                sql,
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getAvatar_Base64(),
                user.getRole()
        );
    }

    @Override
    public User findById(long user_id) {
        String sql = "SELECT username, avatar_base64 FROM users WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{user_id}, resultSet -> {
            if (resultSet.next()) {
                User user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setAvatar_Base64(resultSet.getString("avatar_base64"));
                return user;
            } else {
                return null; // user з вказаним ідентифікатором не знайдено (костиль)
            }
        });
    }

    @Override
    public Long getUserIdByUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        Object[] params = { username };
        try {
            return jdbcTemplate.queryForObject(sql, params, Long.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public String getUserAvatarById(Long userId) {
        String sql = "SELECT avatar_base64 FROM users WHERE id = ?";
        Object[] params = { userId };
        try {
            return jdbcTemplate.queryForObject(sql, params, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
