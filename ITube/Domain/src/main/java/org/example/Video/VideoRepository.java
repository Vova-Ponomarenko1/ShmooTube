package org.example.Video;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository {
    void save(Video video);

    Video findVideoDataById(long videoId);

    long findUserByIdVideo(long videoId); //all info

    List<Thumbnail> getAllThumbnailsWithIds();

    Video findVideoInfoById(long videoId);

    List<Thumbnail> findByTitleContainingIgnoreCase(String searchText);

    List<Thumbnail> findVideoThumbnailByUserId(long userId);

    List<Thumbnail> findMoreVideosByLastVideoIdAndUserId(long lastVideoId, long userId);
}
