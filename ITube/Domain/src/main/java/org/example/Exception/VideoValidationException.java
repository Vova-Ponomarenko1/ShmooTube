package org.example.Exception;

public class VideoValidationException extends RuntimeException {
    public VideoValidationException (String message) {
        super(message);
    }
}
