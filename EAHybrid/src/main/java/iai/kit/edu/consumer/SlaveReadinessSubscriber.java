package iai.kit.edu.consumer;

import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.SlavesConfig;
import iai.kit.edu.producer.EAReadinessPublisher;
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
    SlavesConfig slavesConfig;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("I got a message: " + message.toString());
        RedisAtomicInteger slavesReadyCounter = new RedisAtomicInteger(numberOfSlavesReadyString,template.getConnectionFactory());
        int numberOfSlavesReady = slavesReadyCounter.incrementAndGet();
        if(numberOfSlavesReady == slavesConfig.getNumberOfSlaves()){
            System.out.println("All Slaves Ready");
            slavesConfig.setAllSlavesReady(true);
        }

        if(slavesConfig.isAllSlavesInitialized() && slavesConfig.isAllSlavesReady()){
        eaReadinessPublisher.publish();
        }
    }

}
