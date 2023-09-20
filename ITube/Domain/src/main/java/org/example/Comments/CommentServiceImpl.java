package org.example.Comments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentValidator commentValidator;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void addNewComment(Comment comment) {
        commentValidator.isValidComment(comment.getCommentText());
        commentRepository.addNewComment(comment);
    }

    @Override
    public List<Comment> getCommentsForVideo(Long videoId, int page) {
        return commentRepository.findByVideoId(videoId, page);
    }
}
