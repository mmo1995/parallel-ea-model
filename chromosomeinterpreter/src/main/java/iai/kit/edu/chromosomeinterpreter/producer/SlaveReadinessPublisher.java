package iai.kit.edu.chromosomeinterpreter.producer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import iai.kit.edu.chromosomeinterpreter.config.ConfigResetter;
import iai.kit.edu.chromosomeinterpreter.config.ConstantStrings;
import iai.kit.edu.chromosomeinterpreter.config.IslandConfig;
import iai.kit.edu.chromosomeinterpreter.core.Chromosomeinterpreter;

public class SlaveReadinessPublisher {

    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    IslandConfig islandConfig;

    @Value("${slave.number}")
    private String slaveNumber;

    @Autowired
    RedisMessageListenerContainer redisMessageListenerContainer;
    @Autowired
    ConfigResetter configResetter;
    @Autowired
    static Chromosomeinterpreter chromosomeinterpreter;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("stringTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    @Qualifier("slaveReadyTopic")
    private ChannelTopic topic;

    public void publish() throws IOException {
        redisTemplate.convertAndSend(topic.getTopic(), "Slave ready " + slaveNumber);
    }

}
