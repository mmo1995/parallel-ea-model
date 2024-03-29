package edu.kit.iai.calculation.consumer;

import edu.kit.iai.calculation.config.ConfigResetter;
import edu.kit.iai.calculation.config.ConstantStrings;
import edu.kit.iai.calculation.config.IslandConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class StopSubscribe implements MessageListener {
    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;
    @Autowired
    ConfigResetter configResetter;
    @Autowired
    IslandConfig islandConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void onMessage(Message message, byte[] pattern) {
        ValueOperations<String, String> ops = this.stringRedisTemplate.opsForValue();
        String stopSubscribing = ops.get(ConstantStrings.stopSubscribing + "." + islandConfig.getIslandNumber());
        if (stopSubscribing.equals("stop")) {
            logger.info("Here is stop signal");
            //configResetter.reset();
        }
    }
}
