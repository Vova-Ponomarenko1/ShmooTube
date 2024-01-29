package org.example.Video;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface VideoService {
    void saveVideo(String title, String description, MultipartFile file, MultipartFile photo) throws IOException;

    List<Thumbnail> updateThumbnailsWithBase64DataUri(List<Thumbnail> thumbnails);
}
