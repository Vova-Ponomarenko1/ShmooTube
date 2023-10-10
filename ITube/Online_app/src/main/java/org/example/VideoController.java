package org.example;


import jakarta.servlet.http.HttpServletRequest;
import org.example.Users.User;
import org.example.Users.UserRepository;
import org.example.Video.Thumbnail;
import org.example.Video.Video;
import org.example.Video.VideoRepository;
import org.example.Video.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/ITube")
public class VideoController {
    @Autowired
    private  VideoService videoService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/video")
    public ModelAndView viewVideoPage() {
        return new ModelAndView("UploadVideo");
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("title") String title,
                                              @RequestParam("description") String description,
                                              @RequestParam("file") MultipartFile file,
                                              @RequestParam("imageFile") MultipartFile photo) throws IOException {
        videoService.saveVideo(title, description, file, photo);
        return ResponseEntity.ok("Video uploaded successfully.");
    }
    @GetMapping("/video/{videoId}")
    public ModelAndView viewVideo(@PathVariable Long videoId, Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView("videos");
        List<Thumbnail> getAllThumbnailsWithId = videoService.updateThumbnailsWithBase64DataUri();
        if (authentication != null && authentication.isAuthenticated()) {
            modelAndView.addObject("hideRegisterButton", true);
        } else {
            modelAndView.addObject("hideRegisterButton", false);
        }

        User userInfo = userRepository.findById(videoRepository.findUserByIdVideo(videoId));
        Video videoInfo = videoRepository.findVideoInfoById(videoId);

        modelAndView.addObject("userInfo", userInfo);
        modelAndView.addObject("videoInfo", videoInfo);
        modelAndView.addObject("recommendedVideos", getAllThumbnailsWithId);
        modelAndView.addObject("videoUrl", "/ITube/video/" + videoId + "/stream");

        modelAndView.addObject("comment", null);
        return modelAndView;
    }

    @GetMapping("/video/{videoId}/stream")
    public CompletableFuture<ResponseEntity<InputStreamResource>> getVideo(@PathVariable Long videoId,
            HttpServletRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            long threadId = Thread.currentThread().getId();
            System.out.println("Thread ID: " + threadId);

        Video video = videoRepository.findVideoDataById(videoId);
        if (video == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("video/mp4"));
        headers.set("Accept-Ranges", "bytes");
        long videoSize = video.getVideoData().length;
        String rangeHeader = request.getHeader("Range");
        if (rangeHeader != null) {
            String[] ranges = rangeHeader.substring(6).split("-");
            long start = Long.parseLong(ranges[0]);
            long end = videoSize - 1;
            if (ranges.length > 1) {
                end = Long.parseLong(ranges[1]);
            }
            headers.set("Content-Range", "bytes " + start + "-" + end + "/" + videoSize);
            headers.setContentLength(end - start + 1);
            InputStreamResource videoStream = new InputStreamResource(new ByteArrayInputStream(video.getVideoData(),
                    (int) start, (int) (end - start + 1)));
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .body(videoStream);
        } else {
            InputStreamResource videoStream = new InputStreamResource(new ByteArrayInputStream(video.getVideoData()));
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(videoStream);
        }
    }, executorService);
    }



}
