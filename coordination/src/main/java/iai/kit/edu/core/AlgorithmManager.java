package iai.kit.edu.core;

import iai.kit.edu.config.CounterResetter;
import iai.kit.edu.config.DynamicJobConfig;
import iai.kit.edu.config.JobConfig;
import iai.kit.edu.controller.SlaveController;
import iai.kit.edu.controller.ConfigController;
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
    private SlaveController slaveController;
    @Autowired
    private ConfigController configController;
    @Autowired
    private PopulationController populationController;

    @Autowired
    private CounterResetter counterResetter;
    @Autowired
    private JobConfig jobConfig;

    @Autowired
    private DynamicJobConfig dynamicJobConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void initialize(boolean dynamic) {
        counterResetter.resetCounters();
        if(dynamic){
            islandController.createIslands(dynamicJobConfig.getNumberOfIslands());
        }
        else{
            islandController.createIslands(jobConfig.getNumberOfIslands());
        }
    }

    public void sendConfig(boolean dynamic) {
        if(dynamic){
            try {
                populationController.sendPopulation(true);
                configController.sendDynamicConfig(dynamicJobConfig);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }else{
            try {
                populationController.sendPopulation(false);
                configController.sendConfig(jobConfig);

            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
