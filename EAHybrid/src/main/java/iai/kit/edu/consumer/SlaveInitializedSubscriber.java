package iai.kit.edu.consumer;

import iai.kit.edu.config.SlavesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void onMessage(Message message, byte[] pattern) {
		logger.info(message.toString());
		RedisAtomicInteger slavesInitializedCounter = new RedisAtomicInteger(numberOfSlavesInitializedString,template.getConnectionFactory());
		int numberOfSlavesInitialized = slavesInitializedCounter.incrementAndGet();
		logger.info("Slaves number " +  numberOfSlavesInitialized + " intialized");
		logger.info("Number of required Slaves " +  slavesConfig.getNumberOfSlaves());
		if(numberOfSlavesInitialized == slavesConfig.getNumberOfSlaves()){
			slavesInitializedCounter.set(0);
			logger.info("All Slaves Initialized");
			slavesConfig.setAllSlavesInitialized(true);
		}

	}

}
