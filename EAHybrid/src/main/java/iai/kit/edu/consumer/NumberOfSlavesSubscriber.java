package iai.kit.edu.consumer;

import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.SlavesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class NumberOfSlavesSubscriber implements MessageListener {

    @Autowired
    @Qualifier("integerTemplate")
    RedisTemplate<String, Integer> integerTemplate;

    @Autowired
    SlavesConfig slavesConfig;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("I have got a message:"  + message.toString());
        ValueOperations<String, Integer> ops = this.integerTemplate.opsForValue();
        int numberOfSlaves = ops.get(ConstantStrings.numberOfSlaves);
        System.out.println(" Number of Slaves: " + numberOfSlaves);
        slavesConfig.setNumberOfSlaves(numberOfSlaves);
    }
}
