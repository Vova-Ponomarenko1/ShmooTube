package org.example.Comments;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository {

   void addNewComment (Comment comment);
   List<Comment> findByVideoId(Long videoId, int page);
}
