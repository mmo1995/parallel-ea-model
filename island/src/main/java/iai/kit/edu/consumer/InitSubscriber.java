package iai.kit.edu.consumer;

import iai.kit.edu.config.ConfigResetter;
import iai.kit.edu.config.IslandConfig;
import iai.kit.edu.controller.IslandInitializedController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Receives initialization signal. Needed if a container is used for another optimization task to reset island.
 */
public class InitSubscriber implements MessageListener {
    @Autowired
    IslandInitializedController islandInitializedController;
    @Autowired
    IslandConfig islandConfig;
    @Autowired
    ConfigResetter configResetter;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.trace(message.toString());
        if (islandConfig.getIslandNumber() == Integer.parseInt(message.toString())) {
            logger.info("received init signal");
            configResetter.initialize();
            islandInitializedController.sendInitializedStatus();
        }
    }
}
