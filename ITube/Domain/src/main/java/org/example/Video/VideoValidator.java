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
            throw new VideoValidationException("Відео не існує.");
        }

        long maxVideoSizeInBytes = 2L * 1024 * 1024 * 1024; // 2 ГБ
        if (video.getVideoData().length > maxVideoSizeInBytes) {
            throw new VideoValidationException("Відео занадто велике. Максимальний розмір: 2 ГБ.");
        }

        String extension = StringUtils.getFilenameExtension("dummy." + video.getVideoData().length);
        if (!VALID_VIDEO_EXTENSIONS.contains(extension)) {
            throw new VideoValidationException("Недійсний формат відео. Дозволені лише \"mp3\", \"mp4\", \"mvc\".");
        }
    }

}
