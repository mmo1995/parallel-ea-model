package iai.kit.edu.chromosomeinterpreter.consumer;

import iai.kit.edu.chromosomeinterpreter.core.Chromosomeinterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

public class DateSubscriber implements MessageListener {

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;
    @Autowired
    Chromosomeinterpreter chromosomeinterpreter;

    /**
     * recieving the date to be scheduled
     *
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        chromosomeinterpreter.setDate(message.toString());
    }
}
