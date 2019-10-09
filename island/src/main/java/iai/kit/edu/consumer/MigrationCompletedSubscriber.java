package iai.kit.edu.consumer;

import iai.kit.edu.core.AlgorithmWrapper;
import iai.kit.edu.core.MigrantReplacer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * Pinged from the last island that finishes the migration to start the next epoch
 */
public class MigrationCompletedSubscriber implements MessageListener {

    @Autowired
    private AlgorithmWrapper algorithmWrapper;
    @Autowired
    private MigrantReplacer migrantReplacer;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info("completing migration");
        logger.info("received start signal");
        migrantReplacer.replace();
        algorithmWrapper.startEpoch();
    }
}
