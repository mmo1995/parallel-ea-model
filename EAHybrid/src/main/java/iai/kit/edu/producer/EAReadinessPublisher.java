package iai.kit.edu.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

public class EAReadinessPublisher {
	
	@Autowired
	@Qualifier("stringTemplate")
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	@Qualifier("eaReadyTopic")
	private ChannelTopic topic;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public void publish(){
		logger.info("publishing EA ready!");
		redisTemplate.convertAndSend(topic.getTopic(), "EA ready");
	}

}
