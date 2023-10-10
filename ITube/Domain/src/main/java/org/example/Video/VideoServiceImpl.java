package org.example.Video;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoValidator videoValidator;

    @Override
    @Transactional
    public void saveVideo(String title, String description, MultipartFile file, MultipartFile photo) throws IOException {
        byte[] videoData = file.getBytes();
        byte[] photoData = photo.getBytes();

        Video video = new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoData(videoData);
        video.setThumbnail(photoData);

        videoValidator.videoValidate(video);

        videoRepository.save(video);
    }
    public List<Thumbnail> updateThumbnailsWithBase64DataUri() {
        List<Thumbnail> getAllThumbnailsWithId = videoRepository.getAllThumbnailsWithIds();
        getAllThumbnailsWithId.parallelStream().forEach(thumbnail -> {
            byte[] thumbnailBytes = thumbnail.getThumbnail();
            String dataUri = "data:image/png;base64," + Base64.getEncoder().encodeToString(thumbnailBytes);
            thumbnail.setDataUri(dataUri);
        });
        return getAllThumbnailsWithId;
    }
}
