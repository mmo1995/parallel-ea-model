package iai.kit.edu.chromosomeinterpreter.consumer;

import java.io.IOException;

import iai.kit.edu.chromosomeinterpreter.producer.SlaveReadinessPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import iai.kit.edu.chromosomeinterpreter.producer.SlaveInitializedPublisher;

public class CalculationInitializedSubscriber implements MessageListener {

	@Autowired
	SlaveReadinessPublisher slaveReadinessPublisher;
	
	@Override
	public void onMessage(Message message, byte[] pattern){
		System.out.println("I got a message: " + message.toString());
		try {
			slaveReadinessPublisher.publish();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

}
