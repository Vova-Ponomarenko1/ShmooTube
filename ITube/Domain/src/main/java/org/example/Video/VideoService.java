package org.example.Video;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface VideoService {
    void saveVideo(String title, String description, MultipartFile file, MultipartFile photo) throws IOException;
}
