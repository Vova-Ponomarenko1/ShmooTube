package org.example.TestFunction;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.example.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ITube")
public class LiveBroadcast {//Not working:D
    private Map<String, OutputStream> activeStreams = new HashMap<>();

    @GetMapping("/stream/{userId}")
    public void startStreaming(@PathVariable String userId, HttpServletResponse response) {
        try {
            response.setContentType("video/webm");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Connection", "keep-alive");

            OutputStream outputStream = response.getOutputStream();
            activeStreams.put(userId, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/stopStream/{userId}")
    public void stopStreaming(@PathVariable String userId) {
        try {
            if (activeStreams.containsKey(userId)) {
                OutputStream outputStream = activeStreams.get(userId);
                outputStream.close();
                activeStreams.remove(userId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

