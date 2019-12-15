package iai.kit.edu.consumer;

import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.SlavesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;

public class SlaveInitializedSubscriber implements MessageListener {
	@Qualifier("integerTemplate")
	@Autowired
	RedisTemplate<String, Integer> template;

	@Qualifier("numberOfSlavesInitializedString")
	@Autowired
	String numberOfSlavesInitializedString;

	@Autowired
	SlavesConfig slavesConfig;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		System.out.println("I got a message: " + message.toString());
		RedisAtomicInteger slavesInitializedCounter = new RedisAtomicInteger(numberOfSlavesInitializedString,template.getConnectionFactory());
		int numberOfSlavesInitialized = slavesInitializedCounter.incrementAndGet();
		if(numberOfSlavesInitialized == slavesConfig.getNumberOfSlaves()){
			System.out.println("All Slaves Initialized");
			slavesConfig.setAllSlavesInitialized(true);
		}

	}

}
