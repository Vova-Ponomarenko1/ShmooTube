package org.example;

import org.example.LikeAndSubscriber.SubscribersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
@Repository
public class SubscriberRepositoryImpl implements SubscribersRepository {

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public SubscriberRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void subscribeOrUnsubscribe(Long subscriberId, Long subscribedToId) {
        String checkSubscriptionSql = "SELECT COUNT(*) FROM subscriptions WHERE subscriber_id = ? AND subscribed_to_id = ?";
        int subscriptionCount = jdbcTemplate.queryForObject(checkSubscriptionSql, Integer.class, subscriberId, subscribedToId);

        if (subscriptionCount > 0) {
            String deleteSubscriptionSql = "DELETE FROM subscriptions WHERE subscriber_id = ? AND subscribed_to_id = ?";
            jdbcTemplate.update(deleteSubscriptionSql, subscriberId, subscribedToId);
        } else {
            String insertSubscriptionSql = "INSERT INTO subscriptions (subscriber_id, subscribed_to_id) VALUES (?, ?)";
            jdbcTemplate.update(insertSubscriptionSql, subscriberId, subscribedToId);
        }
    }

    @Override
    public boolean isUserSubscribed(Long subscriberId, Long subscribedToId) {
        String checkSubscriptionSql = "SELECT COUNT(*) FROM subscriptions WHERE subscriber_id = ? " +
            "AND subscribed_to_id = ?";
        int subscriptionCount = jdbcTemplate.queryForObject(checkSubscriptionSql,
            Integer.class, subscriberId, subscribedToId);
        return subscriptionCount > 0;
    }

}
