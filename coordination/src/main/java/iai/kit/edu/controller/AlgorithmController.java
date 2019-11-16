package iai.kit.edu.controller;


import com.google.gson.Gson;
import iai.kit.edu.config.ExperimentConfig;
import iai.kit.edu.config.JobConfig;
import iai.kit.edu.core.AlgorithmManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls start and receiving the result
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ojm")
public class AlgorithmController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AlgorithmManager algorithmManager;
    @Autowired
    private JobConfig jobConfig;

    private List<JobConfig> jobConfigList;

    private boolean experiment;

    /**
     * Receive configuration for one optimization task
     * @param json of configuration
     */
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public void receiveStartConfiguration(@RequestBody String json) {
        experiment = false;
        jobConfig.readFromJson(json);
        logger.info("received job config: " + jobConfig.toString());
        algorithmManager.initialize();
    }

    /**
     * Receive configuration for several optimization task
     * @param json of configurations
     */
    @RequestMapping(value = "/start/jobs", method = RequestMethod.POST)
    public void receiveStartConfigurations(@RequestBody String json) {
        experiment = true;
        Gson gson = new Gson();
        ExperimentConfig experimentConfig = gson.fromJson(json, ExperimentConfig.class);
        int[] numberOfIslands = experimentConfig.getNumberOfIslands();
        int[] populationSizes = experimentConfig.getPopulationSize();
        int[] delays = experimentConfig.getDelay();
        int[] migrationRates = experimentConfig.getMigrationRate();
        String[] topologies = experimentConfig.getTopology();
        int epochTerminationGeneration_1 = experimentConfig.getEpochTerminationGeneration();
        String epochTerminationCriterion_1 = experimentConfig.getEpochTerminationCriterion();
        int epochTerminationEvaluation_1 = experimentConfig.getEpochTerminationEvaluation();
        double epochTerminationFitness_1 = experimentConfig.getEpochTerminationFitness();
        String globalTerminationCriterion_1 =  experimentConfig.getGlobalTerminationCriterion();
        int globalTerminationEpoch_1 = experimentConfig.getGlobalTerminationEpoch();
        int globalTerminationEvaluation_1 = experimentConfig.getGlobalTerminationEvaluation();
        double globalTerminationFitness_1 = experimentConfig.getGlobalTerminationFitness();
        int globalTerminationGeneration_1 = experimentConfig.getGlobalTerminationGeneration();

        jobConfigList = new ArrayList<>();
        for (int i = 0; i < numberOfIslands.length; i++) {
            for (int j = 0; j < populationSizes.length; j++) {
                for (int k = 0; k < delays.length; k++) {
                    for (int l = 0; l < migrationRates.length; l++) {
                        for (int m = 0; m < topologies.length; m++) {
                            JobConfig jobConfigTemp = new JobConfig();
                            jobConfigTemp.setNumberOfIslands(numberOfIslands[i]);
                            jobConfigTemp.setGlobalPopulationSize(populationSizes[j]);
                            jobConfigTemp.setDelay(delays[k]);
                            jobConfigTemp.setMigrationRate(migrationRates[l]);
                            jobConfigTemp.setTopology(topologies[m]);
                            jobConfigTemp.setEpochTerminationCriterion(epochTerminationCriterion_1);
                            jobConfigTemp.setEpochTerminationGeneration(epochTerminationGeneration_1);
                            jobConfigTemp.setEpochTerminationEvaluation(epochTerminationEvaluation_1);
                            jobConfigTemp.setEpochTerminationFitness(epochTerminationFitness_1);
                            jobConfigTemp.setGlobalTerminationCriterion(globalTerminationCriterion_1);
                            jobConfigTemp.setGlobalTerminationEpoch(globalTerminationEpoch_1);
                            jobConfigTemp.setGlobalTerminationEvaluation(globalTerminationEvaluation_1);
                            jobConfigTemp.setGlobalTerminationFitness(globalTerminationFitness_1);
                            jobConfigTemp.setGlobalTerminationEpoch(globalTerminationGeneration_1);
                            jobConfigList.add(jobConfigTemp);
                            // speedupresults.setJobConfig(jobConfig);
                            // speedUpResultsList.add(speedupresults);
                        }
                    }
                }
            }
        }
        jobConfig.readFromExistingJobConfig(jobConfigList.remove(0));
        logger.info("received job config: " + jobConfig.toString());
        algorithmManager.initialize();
    }


    /**
     * Receive result
     * @param resultJson of result
     */
    @RequestMapping(value = "/result", method = RequestMethod.POST)
    public void receiveResult(@RequestBody String resultJson) {
        logger.info("receiving result");
        Gson gson = new Gson();
        List<String> result = gson.fromJson(resultJson, List.class);
        if (experiment && jobConfigList.size() != 0) {
            jobConfig.readFromExistingJobConfig(jobConfigList.remove(0));
            logger.info("received job config: " + jobConfig.toString());
            algorithmManager.initialize();
        }
    }
}
