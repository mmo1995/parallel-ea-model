package iai.kit.edu.producer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import iai.kit.edu.config.ConstantStrings;


/**
 * 
 * publishes the initialized status of a calculation
 *
 */
public class CalculationInitializedPublisher {

	
    @Autowired
    @Qualifier("stringTemplate")
    RedisTemplate<String, String> stringTemplate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void publishInitialized(int islandNumber, int calculationNumber) {
        ChannelTopic topic = new ChannelTopic(ConstantStrings.CalculationInitialized + "." + islandNumber + "." + calculationNumber);
        stringTemplate.convertAndSend(topic.getTopic(), "initialized");
    	logger.info("Calculation initialized with island number " + islandNumber + 
    			" and calculation number " + calculationNumber + ".");
    	
   }

}
