package iai.kit.edu.consumer;

import iai.kit.edu.core.AlgorithmWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * Receives the synchronized start signal for one optimization task
 */
public class StartSubscriber implements MessageListener {

    @Autowired
    private AlgorithmWrapper algorithmWrapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info("received start signal");

        algorithmWrapper.init();
        algorithmWrapper.startEpoch();

    }
}