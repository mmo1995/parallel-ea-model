package iai.kit.edu.core;

import iai.kit.edu.config.CounterResetter;
import iai.kit.edu.config.JobConfig;
import iai.kit.edu.controller.ConfigController;
import iai.kit.edu.controller.InitializerEAController;
import iai.kit.edu.controller.IslandController;
import iai.kit.edu.controller.PopulationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Triggers the core parts of the coordination service:
 *      resetting counters
 *      creating islands
 *      sending intial population and configuration
 */
public class AlgorithmManager {
    @Autowired
    private IslandController islandController;
    @Autowired
    private ConfigController configController;
    @Autowired
    private PopulationController populationController;

    @Autowired
    private CounterResetter counterResetter;
    @Autowired
    private JobConfig jobConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void initialize() {
        counterResetter.resetCounters();
        islandController.createIslands(jobConfig.getNumberOfIslands());

    }

    public void sendConfig() {
        try {
            populationController.sendPopulation();
            configController.sendConfig(jobConfig);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
