package iai.kit.edu.producer;

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



	public void publish(){
		System.out.println("Publishing EA ready:");
		redisTemplate.convertAndSend(topic.getTopic(), "EA ready");
	}

}
