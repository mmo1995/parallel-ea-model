package edu.kit.iai.calculation.consumer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import edu.kit.iai.calculation.core.Calculation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

public class DateSubscriber implements MessageListener {

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;
    @Autowired
    Calculation calculation;

    /**
     * recieving the date to be scheduled
     *
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
    calculation.setDate(message.toString());
    }
}
