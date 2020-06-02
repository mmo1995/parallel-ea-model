package iai.kit.edu.consumer;

import iai.kit.edu.config.IslandConfig;
import iai.kit.edu.controller.MigrationOverheadController;
import iai.kit.edu.core.Chromosome;
import iai.kit.edu.core.MigrantReplacer;
import iai.kit.edu.core.MigrantSelector;
import iai.kit.edu.core.Population;
import iai.kit.edu.producer.MigrantPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.List;

/**
 * receives the ea executiontime of one epoch
 */
public class EAExecutiontimeSubscriber implements MessageListener{


    @Autowired
    MigrationOverheadController migrationOverheadController;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

        @Override
        public void onMessage(Message message, byte[] pattern) {
            logger.debug("received EA Execution time");
            logger.info("EA execution time: " + message.toString() + " seconds");
            migrationOverheadController.addEAExecutiontime(message.toString());
        }
}
