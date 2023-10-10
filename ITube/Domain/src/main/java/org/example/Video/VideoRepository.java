package org.example.Video;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository {
    void save(Video video);

    Video findVideoDataById(long videoId);

    long findUserByIdVideo(long videoId); //знаходить всю інфу про юзера

    List<Thumbnail> getAllThumbnailsWithIds();

    Video findVideoInfoById(long videoId);

    List<Thumbnail> findByTitleContainingIgnoreCase(String searchText);
}
