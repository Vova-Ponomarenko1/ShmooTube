package org.example.LikeAndSubscriber;

import org.springframework.stereotype.Repository;

@Repository
public interface SubscribersRepository {

    void subscribeOrUnsubscribe(Long subscriberId, Long subscribedToId);

    boolean isUserSubscribed(Long subscriberId, Long subscribedToId);
}
