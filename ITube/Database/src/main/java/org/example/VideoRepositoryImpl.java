package org.example;

import org.example.Video.Thumbnail;
import org.example.Video.Video;
import org.example.Video.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Primary
@Repository
public class VideoRepositoryImpl implements VideoRepository {
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public VideoRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Video video) {
        String sql = "INSERT INTO videos (title, description, video_data, thumbnail) VALUES (?, ?, ?,?)";
        jdbcTemplate.update(
                sql,
                video.getTitle(),
                video.getDescription(),
                video.getVideoData(),
                video.getThumbnail()
        );
    }

    @Override
    public Video findVideoDataById(long videoId) {
        String sql = "SELECT video_data FROM videos WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{videoId}, resultSet -> {
            if (resultSet.next()) {
                Video video = new Video();
                video.setVideoData(resultSet.getBytes("video_data"));
                return video;
            } else {
                return null; // Відео з вказаним ідентифікатором не знайдено (костиль)
            }
        });
    }

    @Override
    public Video findVideoInfoById(long videoId) {
        String sql = "SELECT  title, description FROM videos WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{videoId}, resultSet -> {
            if (resultSet.next()) {
                Video video = new Video();
                video.setTitle(resultSet.getString("title"));
                video.setDescription(resultSet.getString("description"));
                return video;
            } else {
                return null; // Відео з вказаним ідентифікатором не знайдено (костиль)
            }
        });
    }

    public long findUserByIdVideo(long videoId) {
        String sql = "SELECT user_id FROM videos WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, videoId);
    }

    @Override
    public List<Thumbnail> getAllThumbnailsWithIds() {
        String sql = "SELECT id, thumbnail, title FROM videos";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            Thumbnail thumbnail = new Thumbnail();
            thumbnail.setId(resultSet.getLong("id"));
            thumbnail.setThumbnail(resultSet.getBytes("thumbnail"));
            thumbnail.setTitle(resultSet.getString("title"));
            return thumbnail;
        });
    }
}
