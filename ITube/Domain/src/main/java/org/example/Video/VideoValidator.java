package org.example.Video;

import org.example.Exception.VideoValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Component
public class VideoValidator {

    private static final List<String> VALID_VIDEO_EXTENSIONS = Arrays.asList("mp3", "mp4", "mvc");
    public void videoValidate(Video video) throws VideoValidationException {
        if (video == null || video.getVideoData().length == 0) {
            throw new VideoValidationException("The video does not exist.");
        }

        long maxVideoSizeInBytes = 2L * 1024 * 1024 * 1024; // 2 ГБ
        if (video.getVideoData().length > maxVideoSizeInBytes) {
            throw new VideoValidationException("The video is too big. Maximum size: 2 GB.");
        }




    }

}
