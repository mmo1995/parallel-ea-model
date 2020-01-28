package iai.kit.edu.consumer;

import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.SlavesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ValueOperations<String, Integer> ops = this.integerTemplate.opsForValue();
        int numberOfSlaves = ops.get(ConstantStrings.numberOfSlaves);
        logger.info("Number of Slaves: " + numberOfSlaves);
        slavesConfig.setNumberOfSlaves(numberOfSlaves);
    }
}
