package iai.kit.edu.consumer;

import iai.kit.edu.controller.IslandReadinessController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import iai.kit.edu.config.ConstantStrings;

public class EAReadinessSubscriber implements MessageListener {

	@Autowired
	IslandReadinessController islandReadinessController;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	public void onMessage(Message message, byte[] pattern) {
		logger.info(message.toString());
		islandReadinessController.sendReadinessStatus(ConstantStrings.slavesReady);
		
	}

}
