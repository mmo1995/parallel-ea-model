package iai.kit.edu.consumer;

import iai.kit.edu.algorithm.AlgorithmStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;


public class StopSubscriber implements MessageListener {


    @Autowired
    AlgorithmStarter algorithmStarter;
    private final Logger logger=LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info("received stop signal");
        algorithmStarter.stop();
        algorithmStarter.setStopeped(true);
    }


}