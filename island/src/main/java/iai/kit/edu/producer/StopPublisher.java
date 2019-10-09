package iai.kit.edu.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

/**
 * Publishes stop signal, i.e. when termination criterion is a certain level of fitness, so that the other
 * islands can stop executing the deployed EA
 */
public class StopPublisher {

    @Autowired
    @Qualifier("stringTemplate")
    RedisTemplate<String, String> stringTemplate;

    @Autowired
    @Qualifier("stopTopic")
    ChannelTopic stopTopic;

    public void publish() {
        stringTemplate.convertAndSend(stopTopic.getTopic(), "stop");
    }

}
