package org.example.LikeAndSubscriber;

import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository {
    void addLikeAndDeleteLike(Long videoId, Long userId);

    void addDislikeAndDeleteDislike(Long videoId, Long userId);
}
