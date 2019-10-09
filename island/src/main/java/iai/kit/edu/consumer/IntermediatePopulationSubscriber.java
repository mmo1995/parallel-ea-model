package iai.kit.edu.consumer;

import iai.kit.edu.core.*;
import iai.kit.edu.producer.MigrantPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.List;

/**
 * Receives intermediate population from EA Service and performs migration
 */
public class IntermediatePopulationSubscriber implements MessageListener {

    @Autowired
    private MigrantSelector migrantSelector;
    @Autowired
    private MigrantPublisher migrantPublisher;
    @Autowired
    private Population population;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info("received intermediate population");
        population.readFromJSON(message.toString());
        List<Chromosome> migrants = migrantSelector.selectMigrants();
        migrantPublisher.publish(migrants);
    }
}