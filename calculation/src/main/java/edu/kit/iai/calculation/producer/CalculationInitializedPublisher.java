package edu.kit.iai.calculation.producer;

import edu.kit.iai.calculation.config.ConfigResetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.web.client.RestTemplate;

public class CalculationInitializedPublisher implements ApplicationRunner{

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    ConfigResetter configResetter;
	@Autowired
	@Qualifier("stringTemplate")

	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	@Qualifier("calculationInitializedTopic")
	private ChannelTopic topic;

	@Override
	public void run(ApplicationArguments args){
        configResetter.initialize();
	}

}
