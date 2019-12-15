package iai.kit.edu.consumer;

import iai.kit.edu.config.SlavesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class NumberOfSlavesSubscriber implements MessageListener {

    @Autowired
    SlavesConfig slavesConfig;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("I have got a message: Number of Slaves: "  + message.toString());
        String stringNumberOfSlaves = message.toString();
        int intNumberOfSlaves = Integer.parseInt(stringNumberOfSlaves);
        slavesConfig.setNumberOfSlaves(intNumberOfSlaves);
    }
}
