package iai.kit.edu.chromosomeinterpreter.config;

import iai.kit.edu.chromosomeinterpreter.consumer.*;
import iai.kit.edu.chromosomeinterpreter.consumer.SlavePopulationSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * Resets the config between two succeeding optimization tasks
 */
public class ConfigResetter {

    @Value("${slave.number}")
    private String slaveNumber;
    @Autowired
    RedisMessageListenerContainer container;
    @Autowired
    InitSubscriber initSubscriber;
    @Autowired
    SlavePopulationSubscriber slavePopulationSubscriber;
    @Autowired
    @Qualifier("initializeIslandsTopic")
    ChannelTopic initializeIslandsTopic;
    @Autowired
    @Qualifier("slavePopulationTopic")
    ChannelTopic slavePopulationTopic;
    @Autowired
    @Qualifier("calculationInitializedTopic")
    ChannelTopic calculationInitializedTopic;
    @Autowired
    @Qualifier("dateTopic")
    ChannelTopic dateTopic;
    @Autowired
    @Qualifier("stopSubschribingTopic")
    ChannelTopic stopSubschribingTopic;
    @Autowired
    DateSubscriber dateSubscriber;
    @Autowired
    StopSubscribe stopSubscribe;
    
    @Autowired
    CalculationInitializedSubscriber calculationInitializedSubscriber;
    @Autowired
    @Qualifier("stringTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    @Qualifier("slaveInitializedTopic")
    private ChannelTopic topic;

    @Autowired
    IslandConfig islandConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void initialize() {
        logger.info("initializing chromosomeinterpreter");
        container.addMessageListener(initSubscriber, initializeIslandsTopic); // need if the container is already existed
        container.addMessageListener(slavePopulationSubscriber, slavePopulationTopic); // read the sub population to be interpreted
        container.addMessageListener(dateSubscriber,dateTopic); // get the date to be scheduled
        container.addMessageListener(calculationInitializedSubscriber, calculationInitializedTopic);
        redisTemplate.convertAndSend(topic.getTopic(), "Slave initialized " + slaveNumber);

        //container.addMessageListener(stopSubscribe,stopSubschribingTopic); // get the date to be scheduled
    }

    public void reset() {
        logger.info("resetting chromosomeinterpreter");
        container.removeMessageListener(initSubscriber, initializeIslandsTopic);
        container.removeMessageListener(slavePopulationSubscriber, slavePopulationTopic);
        container.removeMessageListener(dateSubscriber, dateTopic);
        container.removeMessageListener(calculationInitializedSubscriber, calculationInitializedTopic);
        //container.removeMessageListener(stopSubscribe,stopSubschribingTopic);
        logger.info("finishing resetting chromosomeinterpreter");

    }
}
