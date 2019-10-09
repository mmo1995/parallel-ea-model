package iai.kit.edu.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

/**
 * Publishes signal to all islands that migration is completed
 */
public class MigrationCompletedPublisher {

    @Autowired
    @Qualifier("stringTemplate")
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    @Qualifier("migrationCompletedTopic")
    private ChannelTopic topic;

    public void publish() {

        redisTemplate.convertAndSend(topic.getTopic(), "migration completed");
    }
}
