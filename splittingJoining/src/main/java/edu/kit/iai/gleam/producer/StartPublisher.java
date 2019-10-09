package edu.kit.iai.gleam.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

/**
 * Publishes start signal to all islands
 */
public class StartPublisher {

    @Autowired
    @Qualifier("startTopic")
    ChannelTopic startTopic;

    @Autowired
    @Qualifier("stringTemplate")
    RedisTemplate<String, String> template;

    public void publish(String message) {
        template.convertAndSend(startTopic.getTopic(), message);
    }
}
