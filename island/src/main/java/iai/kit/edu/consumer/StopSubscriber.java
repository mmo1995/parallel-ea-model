package iai.kit.edu.consumer;

import iai.kit.edu.config.ConfigResetter;
import iai.kit.edu.config.IslandConfig;
import iai.kit.edu.controller.MigrationOverheadController;
import iai.kit.edu.controller.ResultController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * Receives the stop signal if one island has finished the optimization task (usually only need for termination
 * criteria where termination is not synchronous.
 */
public class StopSubscriber implements MessageListener {

    @Autowired
    IslandConfig islandConfig;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MigrationOverheadController migrationOverheadController;
    @Autowired
    ResultController resultController;
    @Autowired
    ConfigResetter configResetter;

    public void onMessage(Message message, byte[] pattern) {
        logger.info("received stop signal");
        islandConfig.setStopped(true);
        if(islandConfig.isReceivedIntermediatePopulation()){
            migrationOverheadController.setEndIslandExecution(System.currentTimeMillis());
            migrationOverheadController.sendExecutiontimeToCoordination();
            resultController.sendResult();
            configResetter.reset();
        }


    }


}