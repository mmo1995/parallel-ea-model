package iai.kit.edu.chromosomeinterpreter.config;

import iai.kit.edu.chromosomeinterpreter.consumer.CalculationInitializedSubscriber;
import iai.kit.edu.chromosomeinterpreter.consumer.DateSubscriber;
import iai.kit.edu.chromosomeinterpreter.consumer.InitSubscriber;
import iai.kit.edu.chromosomeinterpreter.consumer.InitialPopulationSubscriber;
import iai.kit.edu.chromosomeinterpreter.consumer.StopSubscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * Resets the config between two succeeding optimization tasks
 */
public class ConfigResetter {

    @Autowired
    RedisMessageListenerContainer container;
    @Autowired
    InitSubscriber initSubscriber;
    @Autowired
    InitialPopulationSubscriber initialPopulationSubscriber;
    @Autowired
    @Qualifier("initializeIslandsTopic")
    ChannelTopic initializeIslandsTopic;
    @Autowired
    @Qualifier("initialPopulationTopic")
    ChannelTopic initialPopulationTopic;
    
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
    IslandConfig islandConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void initialize() {
        logger.info("initializing chromosomeinterpreter");
        container.addMessageListener(initSubscriber, initializeIslandsTopic); // need if the container is already existed
        container.addMessageListener(initialPopulationSubscriber, initialPopulationTopic); // read the sub population to be interpreted
        container.addMessageListener(dateSubscriber,dateTopic); // get the date to be scheduled
        container.addMessageListener(calculationInitializedSubscriber, calculationInitializedTopic);
        //container.addMessageListener(stopSubscribe,stopSubschribingTopic); // get the date to be scheduled
    }

    public void reset() {
        logger.info("resetting chromosomeinterpreter");
        container.removeMessageListener(initialPopulationSubscriber, initialPopulationTopic);
        container.removeMessageListener(dateSubscriber, dateTopic);
        container.removeMessageListener(calculationInitializedSubscriber, calculationInitializedTopic);
        //container.removeMessageListener(stopSubscribe,stopSubschribingTopic);
        logger.info("finishing resetting chromosomeinterpreter");

    }
}
