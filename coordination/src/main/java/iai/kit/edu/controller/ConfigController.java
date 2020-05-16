package iai.kit.edu.controller;

import com.google.gson.Gson;
import iai.kit.edu.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Sends configuration to splittingJoining service to be distributed to the islands
 */
@CrossOrigin(origins = "*")
@Component
public class ConfigController {
    private TopologyConfig topologyConfig = new TopologyConfig();

    @Autowired
    String workspacePath;

    private final Logger logger=LoggerFactory.getLogger(this.getClass());
    public void sendConfig(JobConfig jobConfig) {
        logger.info("sending config");
        MigrationConfig migrationConfig = jobConfig.generateMigrationConfig();
        AlgorithmConfig algorithmConfig=new GLEAMConfig(workspacePath,jobConfig.getDelay());
        algorithmConfig.setDemeSize(jobConfig.getDemeSize());
        algorithmConfig.readFiles();
        List<List<String>> neighbors = topologyConfig.getNeighbors(jobConfig.getNumberOfIslands(), jobConfig.getTopology());
        Gson gson = new Gson();
        sendToSplittingJoining(gson.toJson(migrationConfig), "migration");
        sendToSplittingJoining(gson.toJson(algorithmConfig), "algorithm");
        sendToSplittingJoining(gson.toJson(neighbors), "neighbors");
        logger.info("completing config sending");
    }
    public void sendDynamicConfig(DynamicJobConfig dynamicJobConfig) {
        logger.info("sending config");
        MigrationConfig[] migrationConfigs = dynamicJobConfig.generateMigrationConfig(dynamicJobConfig.getNumberOfIslands());
        AlgorithmConfig[] algorithmConfigs = new GLEAMConfig[dynamicJobConfig.getDemeSize().length];
        for(int i = 0; i< dynamicJobConfig.getDemeSize().length; i++){
            AlgorithmConfig algorithmConfig = new GLEAMConfig(workspacePath, dynamicJobConfig.getDelay());
            algorithmConfig.setDemeSize(dynamicJobConfig.getDemeSize()[i]);
            algorithmConfig.readFiles();
            algorithmConfigs[i] = algorithmConfig;
        }

        List<List<String>> neighbors = topologyConfig.getNeighbors(dynamicJobConfig.getNumberOfIslands(), dynamicJobConfig.getTopology());
        Gson gson = new Gson();
        sendToSplittingJoining(gson.toJson(migrationConfigs), "migration");
        sendToSplittingJoining(gson.toJson(algorithmConfigs), "dynamic/algorithm");
        sendToSplittingJoining(gson.toJson(neighbors), "dynamic/neighbors");
        logger.info("completing config sending");
    }

    /**
     * Send each part of the configuration separately
     * @param json configuration
     * @param path destination of configuration
     */
    private void sendToSplittingJoining(String json, String path) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(json, headers);
            logger.debug("sending dynamic config for " + path);
            ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.splittingJoiningURL + "/sjs/config/" + path, entity, String.class);

    }
}
