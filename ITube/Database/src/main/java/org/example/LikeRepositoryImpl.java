package org.example;

import org.example.LikeAndSubscriber.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LikeRepositoryImpl implements LikeRepository {

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public LikeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void addLikeAndDeleteLike(Long videoId, Long userId) {
        String checkLikeSql = "SELECT COUNT(*) FROM video_likes WHERE video_id = ? AND user_id = ?";
        int likeCount = jdbcTemplate.queryForObject(checkLikeSql, Integer.class, videoId, userId);

        if (likeCount > 0) {
            String deleteLikeSql = "DELETE FROM video_likes WHERE video_id = ? AND user_id = ?";
            jdbcTemplate.update(deleteLikeSql, videoId, userId);
        } else {
            String checkDislikeSql = "SELECT COUNT(*) FROM video_dislikes WHERE video_id = ? AND user_id = ?";
            int dislikeCount = jdbcTemplate.queryForObject(checkDislikeSql, Integer.class, videoId, userId);
            if (dislikeCount > 0) {
                String deleteDislikeSql = "DELETE FROM video_dislikes WHERE video_id = ? AND user_id = ?";
                jdbcTemplate.update(deleteDislikeSql, videoId, userId);
            }
            String insertLikeSql = "INSERT INTO video_likes (video_id, user_id) VALUES (?, ?)";
            jdbcTemplate.update(insertLikeSql, videoId, userId);
        }
    }

    @Override
    public void addDislikeAndDeleteDislike(Long videoId, Long userId) {
        String checkDislikeSql = "SELECT COUNT(*) FROM video_dislikes WHERE video_id = ? AND user_id = ?";
        int dislikeCount = jdbcTemplate.queryForObject(checkDislikeSql, Integer.class, videoId, userId);

        if (dislikeCount > 0) {
            String deleteDislikeSql = "DELETE FROM video_dislikes WHERE video_id = ? AND user_id = ?";
            jdbcTemplate.update(deleteDislikeSql, videoId, userId);
        } else {
            String checkLikeSql = "SELECT COUNT(*) FROM video_likes WHERE video_id = ? AND user_id = ?";
            int likeCount = jdbcTemplate.queryForObject(checkLikeSql, Integer.class, videoId, userId);
            if (likeCount > 0) {
                String deleteLikeSql = "DELETE FROM video_likes WHERE video_id = ? AND user_id = ?";
                jdbcTemplate.update(deleteLikeSql, videoId, userId);
            }
            String insertDislikeSql = "INSERT INTO video_dislikes (video_id, user_id) VALUES (?, ?)";
            jdbcTemplate.update(insertDislikeSql, videoId, userId);
        }
    }

}
