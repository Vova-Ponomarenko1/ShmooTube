package org.example;

import org.example.Comments.Comment;
import org.example.Comments.CommentService;
import org.example.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ITube")
public class CommentsController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;
    @ResponseBody
    @GetMapping("/video/{videoId}/comments")
    public List<Comment> getComments(@PathVariable Long videoId,
                                     @RequestParam("page") int page) {

        List<Comment> comments = commentService.getCommentsForVideo(videoId,page);
        return comments;
    }


    @PostMapping("/new_comment")
    public void setComment(@RequestParam("comment") String commentText,
                           @RequestParam("videoId") Long videoId,
                           Authentication authentication) {
        System.out.println(videoId);
        System.out.println(commentText);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userService.getUserIdByUsername(userDetails.getUsername());
        Comment comments = new Comment(commentText, userId, videoId);
        commentService.addNewComment(comments);
    }


}
