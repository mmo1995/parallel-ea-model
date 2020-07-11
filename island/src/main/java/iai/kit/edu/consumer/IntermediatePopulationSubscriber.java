package iai.kit.edu.consumer;

import iai.kit.edu.config.ConfigResetter;
import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.IslandConfig;
import iai.kit.edu.controller.MigrationOverheadController;
import iai.kit.edu.controller.ResultController;
import iai.kit.edu.core.*;
import iai.kit.edu.producer.MigrantPublisher;
import iai.kit.edu.producer.StopPublisher;
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
    AlgorithmWrapper algorithmWrapper;
    @Autowired
    private MigrantSelector migrantSelector;
    @Autowired
    private MigrantPublisher migrantPublisher;
    @Autowired
    private StopPublisher stopPublisher;
    @Autowired
    MigrationOverheadController migrationOverheadController;
    @Autowired
    ResultController resultController;
    @Autowired
    ConfigResetter configResetter;
    @Autowired
    private Population population;
    @Autowired
    IslandConfig islandConfig;
    @Autowired
    MigrantReplacer migrantReplacer;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info("received intermediate population");
        islandConfig.setReceivedIntermediatePopulation(true);
        population.readFromJSON(message.toString());
        List<Chromosome> migrants = migrantSelector.selectMigrants();
        migrantPublisher.publish(migrants);
        if(algorithmWrapper.isGlobalTerminationCriterionReached() && !islandConfig.isStopped() && islandConfig.getMigrationConfig().getGlobalTerminationCriterion().equals(ConstantStrings.terminationFitness)){
            stopPublisher.publish();
        } else if(islandConfig.isStopped()){
            migrationOverheadController.setEndIslandExecution(System.currentTimeMillis());
            migrationOverheadController.sendExecutiontimeToCoordination();
            resultController.sendResult();
            configResetter.reset();

        } else if(islandConfig.getMigrationConfig().isAsyncMigration()){
            migrantReplacer.checkAsyncMigration();
        }

    }
}