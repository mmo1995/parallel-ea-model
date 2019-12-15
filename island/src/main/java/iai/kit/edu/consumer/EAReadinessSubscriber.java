package iai.kit.edu.consumer;

import iai.kit.edu.controller.IslandReadinessController;
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
	@Override
	public void onMessage(Message message, byte[] pattern) {
		System.out.println("I got message: " + message.toString());
		islandReadinessController.sendReadinessStatus(ConstantStrings.slavesReady);
		
	}

}
