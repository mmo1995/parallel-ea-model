package edu.kit.iai.calculation.consumer;

import edu.kit.iai.calculation.config.ConfigResetter;
import edu.kit.iai.calculation.config.ConstantStrings;
import edu.kit.iai.calculation.config.IslandConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;

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

    private RedisAtomicInteger readyStatus;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.trace(message.toString());
        if (Integer.parseInt(islandConfig.getIslandNumber()) == Integer.parseInt(message.toString())) {
            logger.trace("received init signal");
            configResetter.initialize();
        }
    }
}
