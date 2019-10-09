package iai.kit.edu.consumer;

import com.google.gson.Gson;
import iai.kit.edu.algorithm.AlgorithmStarter;
import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.EAEpochConfig;
import iai.kit.edu.producer.IntermediatePopulationPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.File;

/**
 * Receives configuration for one epoch
 */
public class EAEpochSubscriber implements MessageListener {
    @Autowired
    AlgorithmStarter algorithmStarter;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private EAEpochConfig eaEpochConfig;

    @Autowired
    File populationFile;

    @Autowired
    IntermediatePopulationPublisher intermediatePopulationPublisher;

     @Autowired
     File populationIntialFile;
    /**
     * After receiving the configuration for one epoch, the algorithmStarter is configured and afterwards
     * one epoch executed and the intermediate population is sent back to Migration & Synchronization Service
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info("received epoch configuration");
        Gson gson = new Gson();
        algorithmStarter.reset();
        this.eaEpochConfig = gson.fromJson(message.toString(), EAEpochConfig.class);
        algorithmStarter.setPopulationSize(eaEpochConfig.getPopulationSize());
        algorithmStarter.setDelay(eaEpochConfig.getDelay());
        setTerminationCriterion();
        this.eaEpochConfig.getPopulation().writeInitialPopulation(populationIntialFile);
        algorithmStarter.start();
        this.eaEpochConfig.getPopulation().read(populationFile);
        intermediatePopulationPublisher.publishIntermediatePopulation(this.eaEpochConfig.getPopulation());
    }

    private void setTerminationCriterion() {
        switch (this.eaEpochConfig.getEpochTerminationCriterion()) {
            case (ConstantStrings.terminationEvaluation):
                algorithmStarter.setTerminationEvaluation(eaEpochConfig.getEpochTerminationEvaluation());
                break;
            case (ConstantStrings.terminationFitness):
                algorithmStarter.setTerminationFitness(eaEpochConfig.getEpochTerminationFitness());
                break;
            case (ConstantStrings.terminationGeneration):
                algorithmStarter.setTerminationGeneration(eaEpochConfig.getEpochTerminationGeneration());
                break;
            case (ConstantStrings.terminationTime):
                algorithmStarter.setTerminationTime(eaEpochConfig.getEpochTerminationTime());
                break;
        }
    }
}
