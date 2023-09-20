package org.example.Comments;

import org.example.Exception.CommentValidationException;
import org.springframework.stereotype.Component;

@Component
public class CommentValidator {


    public void isValidComment(String commentText) {
        if (commentText == null || commentText.isEmpty()) {
            throw new CommentValidationException("An empty comment is not allowed");
        }
        int commentLength = commentText.length();
        if (commentLength < 1 || commentLength > 3000) {
            throw new CommentValidationException("Comment length does not meet the 3000 character maximum limit");
        }
    }
}
