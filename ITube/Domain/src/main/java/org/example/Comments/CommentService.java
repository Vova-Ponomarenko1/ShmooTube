package org.example.Comments;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {

    void addNewComment(Comment comment);

    List<Comment> getCommentsForVideo(Long videoId, int page);

}
