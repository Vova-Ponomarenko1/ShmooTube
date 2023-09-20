package org.example.Video;

import org.example.Video.Video;
import org.example.Video.VideoRepository;
import org.example.Video.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

        videoValidator.videoValidate(video); // МОЖЛИВІ БАГИ НЕ ЗАТЕСТИВ ТАКОЖ НЕ ЗАВЕРЕШНА

        videoRepository.save(video);
    }
}
