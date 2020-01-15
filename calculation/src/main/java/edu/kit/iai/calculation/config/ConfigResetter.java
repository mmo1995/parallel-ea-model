package edu.kit.iai.calculation.config;

import edu.kit.iai.calculation.consumer.CalculationConfigSubscriber;
import edu.kit.iai.calculation.consumer.DateSubscriber;
import edu.kit.iai.calculation.consumer.InitSubscriber;
import edu.kit.iai.calculation.consumer.StopSubscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * Resets the config between two succeeding optimization tasks
 */
public class ConfigResetter implements ApplicationRunner {

    @Autowired
    RedisMessageListenerContainer container;
    @Autowired
    @Qualifier("initializeCalculationTopic")
    ChannelTopic initializeCalculationTopic;
    @Autowired
    @Qualifier("calculationConfigTopic")
    ChannelTopic calculationConfigTopic;
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
    CalculationConfigSubscriber calculationConfigSubscriber;
    @Autowired
    InitSubscriber initSubscriber;


    @Autowired
    IslandConfig islandConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void initialize() {
        logger.info("initializing calculation");
        container.addMessageListener(initSubscriber, initializeCalculationTopic); // need if the container is already existed
        container.addMessageListener(calculationConfigSubscriber, calculationConfigTopic);// read the sub population to be calculated
        container.addMessageListener(dateSubscriber, dateTopic);// get the date to be scheduled
      //  container.addMessageListener(stopSubscribe,stopSubschribingTopic); // get the date to be scheduled


    }

    public void reset() {
        logger.info("resetting calculation");
        container.removeMessageListener(calculationConfigSubscriber, calculationConfigTopic);
        container.removeMessageListener(dateSubscriber, dateTopic);
        container.removeMessageListener(stopSubscribe,stopSubschribingTopic);

    }

    @Override
    public void run(ApplicationArguments applicationArguments) {
        this.initialize();
    }
}
