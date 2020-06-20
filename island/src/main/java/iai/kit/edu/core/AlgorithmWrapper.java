package iai.kit.edu.core;

import iai.kit.edu.config.ConfigResetter;
import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.EAEpochConfig;
import iai.kit.edu.config.IslandConfig;
import iai.kit.edu.consumer.MigrantSubscriber;
import iai.kit.edu.controller.MigrationOverheadController;
import iai.kit.edu.controller.ResultController;
import iai.kit.edu.producer.EAEpochPublisher;
import iai.kit.edu.producer.MigrantPublisher;
import iai.kit.edu.producer.StopPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.AsyncRestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * Wraps the whole execution of the Coarse-Grained Model
 */
public class AlgorithmWrapper {

    @Autowired
    MigrantSelector migrantSelector;
    @Autowired
    MigrationOverheadController migrationOverheadController;
    @Autowired
    IslandConfig islandConfig;
    @Autowired
    MigrantSubscriber migrantSubscriber;
    @Autowired
    MigrantPublisher migrantPublisher;
    @Autowired
    ResultController resultController;
    @Autowired
    Population population;
    @Autowired
    ConfigResetter configResetter;
    @Autowired
    EAEpochConfig eaEpochConfig;
    @Autowired
    EAEpochPublisher eaEpochPublisher;
    @Autowired
    RestTemplate restTemplate;

    private String globalTerminationCriterion;
    private int globalTerminationEpoch;
    private int globalTerminationEpochCounter = 0;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void init() {
        logger.debug("initializing algorithm");
        islandConfig.setStopped(false);
        eaEpochConfig.setPopulationSize(this.getIslandPopulationSize());
        eaEpochConfig.setDelay(islandConfig.getAlgorithmConfig().getDelay());
        setTerminationCriterion();
        eaEpochConfig.setDemeSize(islandConfig.getAlgorithmConfig().getDemeSize());
        eaEpochConfig.setAcceptanceRuleForOffspring(islandConfig.getAlgorithmConfig().getAcceptanceRuleForOffspring());
        eaEpochConfig.setRankingParameter(islandConfig.getAlgorithmConfig().getRankingParameter());
        eaEpochConfig.setInitStrategy(islandConfig.getAlgorithmConfig().getInitStrategy());
        eaEpochConfig.setAmountFitness(islandConfig.getAlgorithmConfig().getAmountFitness());
    }

    /*
        If global population size is not divisible by the number of islands the first n islands will have the usual
        population size per island + 1
     */
    private int getIslandPopulationSize() {
        int globalPopulationSize = islandConfig.getMigrationConfig().getGlobalPopulationSize();
        int numberOfIslands = islandConfig.getMigrationConfig().getNumberOfIslands();
        int islandPopulationSize = globalPopulationSize / numberOfIslands;
        int remainder = globalPopulationSize % numberOfIslands;
        if (this.islandConfig.getIslandNumber() <= remainder) {
            islandPopulationSize++;
        }
        return islandPopulationSize;
    }

    private void setTerminationCriterion() {
        this.setGlobalTerminationCriterion();
        this.setEpochTerminationCriterion();

    }

    private void setGlobalTerminationCriterion() {
        this.globalTerminationCriterion = islandConfig.getMigrationConfig().getGlobalTerminationCriterion();
        this.globalTerminationEpoch = islandConfig.getMigrationConfig().getGlobalTerminationEpoch();
    }

    private void setEpochTerminationCriterion() {
        eaEpochConfig.setEpochTerminationCriterion(islandConfig.getMigrationConfig().getEpochTerminationCriterion());
        eaEpochConfig.setEpochTerminationEvaluation(islandConfig.getMigrationConfig().getEpochTerminationEvaluation());
        eaEpochConfig.setEpochTerminationFitness(islandConfig.getMigrationConfig().getEpochTerminationFitness());
        eaEpochConfig.setEpochTerminationGeneration(islandConfig.getMigrationConfig().getEpochTerminationGeneration());
        eaEpochConfig.setEpochTerminationTime(islandConfig.getMigrationConfig().getEpochTerminationTime());
    }

    /**
     * Starts epoch when migration is completed
     */
    public void startEpoch() {
        synchronized (population) {
            if (isGlobalTerminationCriterionReached() || islandConfig.isStopped()) {
                migrationOverheadController.setEndIslandExecution(System.currentTimeMillis());
                migrationOverheadController.sendExecutiontimeToCoordination();
                resultController.sendResult();
                configResetter.reset();

            } else {

                eaEpochConfig.setPopulation((GLEAMPopulation) population);
                logger.info("starting epoch ");
                eaEpochPublisher.publishEAEpochConfig(eaEpochConfig);
                eaEpochConfig.deletePopulation();
            }
        }

    }

    public boolean isGlobalTerminationCriterionReached() {
        if (this.globalTerminationCriterion.equals(ConstantStrings.terminationFitness)) {
            double fitnessLimit = this.islandConfig.getMigrationConfig().getGlobalTerminationFitness();
            boolean fitnessLimitReached = population.isFitnessLimitReached(fitnessLimit);
            if (fitnessLimitReached) {
                return true;
            }
            return false;
        } else {
            if (this.globalTerminationEpochCounter >= this.globalTerminationEpoch) {
                return true;
            } else {
                this.globalTerminationEpochCounter++;
                return false;
            }
        }
    }

    public void reset() {
        this.globalTerminationEpochCounter = 0;
    }
}
