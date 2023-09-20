package org.example.Users;

import org.example.Exception.UserValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Component
public class UserValidator {

    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024; // 10MB

    public void validateUser(User user) {
        validateName(user.getUsername());
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
        validateImage(user.getAvatar());
    }

    public void validateName(String name) throws UserValidationException {
        String nameRegex = "^[a-zA-Z]+$";
        if (!name.matches(nameRegex)) {
            throw new UserValidationException("Invalid name format. Name should contain only English letters.");
        }
        if (name.length() < 3 || name.length() > 21) {
            throw new UserValidationException("Invalid name length. Name should be between 3 and 20 characters.");
        }
    }

    private void validateEmail(String email) throws UserValidationException {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(emailRegex)) {
            throw new UserValidationException("Invalid email format.");
        }
    }

    public void validatePassword(String password) {
        if (password.length() < 6) {
            throw new UserValidationException("Password must be at least 6 characters long");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new UserValidationException("Password must contain at least one uppercase letter");
        }
    }

    public void validateImage(MultipartFile image) {
        String filePhoto = image.getOriginalFilename();
        if (!StringUtils.hasText(filePhoto) ||
            !Arrays.asList("jpg", "jpeg", "png").contains(StringUtils.getFilenameExtension(filePhoto))) {
            throw new UserValidationException("Invalid image format. Only \"jpg\", \"jpeg\", \"png\" are allowed.");
        }
        if (image.getSize() > MAX_IMAGE_SIZE) {
            throw new UserValidationException("Image file is too large. Maximum allowed size is 10 MB.");
        }
    }
}
