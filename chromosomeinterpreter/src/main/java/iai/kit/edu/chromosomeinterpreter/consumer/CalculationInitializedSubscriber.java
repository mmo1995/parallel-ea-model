package iai.kit.edu.chromosomeinterpreter.consumer;

import java.io.IOException;

import iai.kit.edu.chromosomeinterpreter.producer.SlaveReadinessPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import iai.kit.edu.chromosomeinterpreter.producer.SlaveInitializedPublisher;

public class CalculationInitializedSubscriber implements MessageListener {

	@Autowired
	SlaveReadinessPublisher slaveReadinessPublisher;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void onMessage(Message message, byte[] pattern){
		logger.info(message.toString());
		try {
			slaveReadinessPublisher.publish();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

}
