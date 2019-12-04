package iai.kit.edu.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import iai.kit.edu.config.ConstantStrings;

/**
 * Subscribes to number of calculations initialized
 */
public class CalculationInitializedSubscriber implements MessageListener {

    @Autowired
    @Qualifier("stringTemplate")
    RedisTemplate<String, String> stringTemplate;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void onMessage(Message message, byte[] pattern) {
    	ValueOperations<String, String> ops = this.stringTemplate.opsForValue();
    	String numberOfCalculations = ops.get(ConstantStrings.numberOfCalculations);
    	logger.info("NUMBER of calculations required:" + numberOfCalculations);
    }
}