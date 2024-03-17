package org.example.VideoControlers;


import jakarta.servlet.http.HttpServletRequest;
import org.example.LikeAndSubscriber.SubscribersRepository;
import org.example.Security.AuthenticationHandler;
import org.example.Users.User;
import org.example.Users.UserRepository;
import org.example.Users.UserService;
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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/ITube")
public class VideoController {
    private VideoService videoService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private VideoRepository videoRepository;
    private UserRepository userRepository;

    private UserService userService;

    private SubscribersRepository subscribersRepository;

    private AuthenticationHandler authenticationHandler;
    @Autowired
    public VideoController(VideoService videoService, VideoRepository videoRepository, UserRepository userRepository, UserService userService, SubscribersRepository subscribersRepository, AuthenticationHandler authenticationHandler) {
        this.videoService = videoService;
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.subscribersRepository = subscribersRepository;
        this.authenticationHandler = authenticationHandler;
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
        List<Thumbnail> getAllThumbnailsWithId = videoService.updateThumbnailsWithBase64DataUri(
            videoRepository.getAllThumbnailsWithIds());
        long userId = videoRepository.findUserByIdVideo(videoId);

        if (authentication != null && authentication.isAuthenticated()) {
            Long subscriberId = authenticationHandler.getUserIdFromAuthentication(authentication);
            modelAndView.addObject("subscribersButton", subscribersRepository
                .isUserSubscribed(userId, subscriberId));

            modelAndView.addObject("hideRegisterButton", true);
        } else {
            modelAndView.addObject("subscribersButton", false);
            modelAndView.addObject("hideRegisterButton", false);
        }

        User userInfo = userRepository.findById(userId);
        Video videoInfo = videoRepository.findVideoInfoById(videoId);
        userInfo.setId(userId);

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
