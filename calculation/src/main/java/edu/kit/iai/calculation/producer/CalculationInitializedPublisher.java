package edu.kit.iai.calculation.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

public class CalculationInitializedPublisher implements ApplicationRunner{

	@Autowired
	@Qualifier("stringTemplate")
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	@Qualifier("calculationInitializedTopic")
	private ChannelTopic topic;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		redisTemplate.convertAndSend(topic.getTopic(), "Calculation initialized");
	}

}
