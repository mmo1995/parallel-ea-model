package iai.kit.edu.chromosomeinterpreter.consumer;

import iai.kit.edu.chromosomeinterpreter.config.ConfigResetter;
import iai.kit.edu.chromosomeinterpreter.config.ConstantStrings;
import iai.kit.edu.chromosomeinterpreter.config.IslandConfig;
import iai.kit.edu.chromosomeinterpreter.producer.SlaveInitializedPublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;

import java.io.IOException;

/**
 * Receives initialization signal. Needed if a container is used for another optimization task to reset island.
 */
public class InitSubscriber implements MessageListener {
    @Autowired
    IslandConfig islandConfig;
    @Autowired
    ConfigResetter configResetter;
    @Autowired
    @Qualifier("integerTemplate")
    RedisTemplate<String, Integer> integerTemplate;
    
	@Autowired
	SlaveInitializedPublisher slaveInitializedPublisher;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.trace(message.toString());

        if (Integer.parseInt(islandConfig.getIslandNumber()) == Integer.parseInt(message.toString())) {
            logger.info("received init signal");
            configResetter.initialize();
            try {
            	slaveInitializedPublisher.publish();
             } catch (IOException e) {
                e.printStackTrace();
             }
        }
    }
}
