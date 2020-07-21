package iai.kit.edu.consumer;

import iai.kit.edu.config.ConfigResetter;
import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.IslandConfig;
import iai.kit.edu.controller.MigrationOverheadController;
import iai.kit.edu.controller.ResultController;
import iai.kit.edu.core.AlgorithmWrapper;
import iai.kit.edu.core.MigrantReplacer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * Pinged from the last island that finishes the migration to start the next epoch
 */
public class MigrationCompletedSubscriber implements MessageListener {

    @Autowired
    AlgorithmWrapper algorithmWrapper;
    @Autowired
    MigrantReplacer migrantReplacer;
    @Autowired
    MigrationOverheadController migrationOverheadController;
    @Autowired
    ResultController resultController;
    @Autowired
    ConfigResetter configResetter;
    @Autowired
    IslandConfig islandConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info("completing migration");
        migrantReplacer.replace();
        if (!algorithmWrapper.isGlobalTerminationCriterionReached() && !islandConfig.isStopped()) {
            algorithmWrapper.startEpoch();
        } else if(algorithmWrapper.isGlobalTerminationCriterionReached() && !islandConfig.getMigrationConfig().getGlobalTerminationCriterion().equals(ConstantStrings.terminationFitness)){
            migrationOverheadController.setEndIslandExecution(System.currentTimeMillis());
            migrationOverheadController.sendExecutiontimeToCoordination();
            resultController.sendResult();
            configResetter.reset();
        }
    }
}
