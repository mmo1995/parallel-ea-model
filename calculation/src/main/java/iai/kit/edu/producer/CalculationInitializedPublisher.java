package iai.kit.edu.producer;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import iai.kit.edu.config.ConstantStrings;


/**
 * 
 * publishes the initialized status of a calculation
 *
 */
public class CalculationInitializedPublisher {
	@Value("${island.number}")
	int islandNumber;
	
	@Value("${calculation.number}")
	int calculationNumber;
	
    @Autowired
    @Qualifier("stringTemplate")
    RedisTemplate<String, String> stringTemplate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void publishInitialized() {
    	logger.info("Calculation Initialized with Island number:" + islandNumber + " and calculationNumber: "
    			+ calculationNumber);
   }

}
