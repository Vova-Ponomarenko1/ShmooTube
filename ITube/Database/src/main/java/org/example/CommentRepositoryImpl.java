package org.example;

import org.example.Comments.Comment;
import org.example.Comments.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public CommentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void addNewComment(Comment comment) {
        String sql = "INSERT INTO comments (video_id, user_id, text) VALUES (?, ?, ?)";
        jdbcTemplate.update(
            sql,
            comment.getVideoId(),
            comment.getUserId(),
            comment.getCommentText());
    }

    @Override
    public List<Comment> findByVideoId(Long videoId, int page) {
        int pageSize = 3;  // Кількість коментарів на одній сторінці
        int offset = (page - 1) * pageSize;  // Обчислення значення OFFSET

        String sql = "SELECT c.comment_id AS comment_id, c.user_id, c.video_id, c.text, u.avatar_base64, u.username" +
            " AS commentator_name " +
            "FROM comments c " +
            "INNER JOIN users u ON c.user_id = u.id " +
            "WHERE c.video_id = ? LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{videoId, pageSize, offset}, (resultSet, rowNum) -> {
            Comment comment = new Comment();
            comment.setId(resultSet.getLong("comment_id"));
            comment.setUserId(resultSet.getLong("user_id"));
            comment.setVideoId(resultSet.getLong("video_id"));
            comment.setCommentText(resultSet.getString("text"));
            comment.setCommentatorAvatar(resultSet.getString("avatar_base64"));
            comment.setCommentatorName(resultSet.getString("commentator_name"));
            return comment;
        });
    }
}
