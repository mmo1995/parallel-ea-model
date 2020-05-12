package iai.kit.edu.consumer;

import iai.kit.edu.algorithm.AlgorithmStarter;
import iai.kit.edu.config.SlavesConfig;
import iai.kit.edu.controller.SlavesReadinessController;
import iai.kit.edu.producer.EAReadinessPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;

public class SlaveReadinessSubscriber implements MessageListener {

    @Qualifier("integerTemplate")
    @Autowired
    RedisTemplate<String, Integer> template;

    @Qualifier("numberOfSlavesReadyString")
    @Autowired
    String numberOfSlavesReadyString;

    @Autowired
    EAReadinessPublisher eaReadinessPublisher;

    @Autowired
    AlgorithmStarter algorithmStarter;

    @Autowired
    SlavesConfig slavesConfig;

    @Autowired
    SlavesReadinessController slavesReadinessController;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info(message.toString());
        RedisAtomicInteger slavesReadyCounter = new RedisAtomicInteger(numberOfSlavesReadyString,template.getConnectionFactory());
        int numberOfSlavesReady = slavesReadyCounter.incrementAndGet();
        logger.info("Slaves number " +  numberOfSlavesReady + " Ready");
        logger.info("Number of required Slaves " +  slavesConfig.getNumberOfSlaves());
        if(numberOfSlavesReady == slavesConfig.getNumberOfSlaves()){
            slavesReadyCounter.set(0);
            logger.info("All Slaves Ready");
            slavesConfig.setAllSlavesReady(true);
            slavesReadinessController.sendReadinessStatus(String.valueOf(numberOfSlavesReady));
        }

        if(slavesConfig.isAllSlavesInitialized() && slavesConfig.isAllSlavesReady()){
            algorithmStarter.setFirstEpoch(true);
            eaReadinessPublisher.publish();
        }
    }

}
