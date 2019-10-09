package iai.kit.edu.consumer;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * Subscriber to intermediate populations, not completely implemented
 */
public class IntermediatePopulationSubscriber implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("Received >> " + message +  ", " + Thread.currentThread().getName() );
    }
}
