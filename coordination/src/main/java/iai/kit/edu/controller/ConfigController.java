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
        algorithmConfig.setAcceptanceRuleForOffspring(jobConfig.getAcceptRuleForOffspring());
        algorithmConfig.setRankingParameter(jobConfig.getRankingParameter());
        algorithmConfig.setInitStrategy(jobConfig.getInitialSelectionPolicy());
        algorithmConfig.setAmountFitness(jobConfig.getAmountFitness());
        algorithmConfig.readFiles(jobConfig.getMinimalHammingDistance(), jobConfig.getEvoFileName());
        List<List<String>> neighbors = topologyConfig.getNeighbors(jobConfig.getNumberOfIslands(), jobConfig.getTopology());
        Gson gson = new Gson();
        sendToSplittingJoining(gson.toJson(migrationConfig), "migration");
        sendToSplittingJoining(gson.toJson(algorithmConfig), "algorithm");
        sendToSplittingJoining(gson.toJson(neighbors), "neighbors");
        logger.info("completing config sending");
    }
    public void sendHeteroConfig(HeteroJobConfig heteroJobConfig) {
        logger.info("sending config");
        MigrationConfig[] migrationConfigs = heteroJobConfig.generateMigrationConfig(heteroJobConfig.getNumberOfIslands());
        AlgorithmConfig[] algorithmConfigs = new GLEAMConfig[heteroJobConfig.getDemeSize().length];
        for(int i = 0; i< heteroJobConfig.getDemeSize().length; i++){
            AlgorithmConfig algorithmConfig = new GLEAMConfig(workspacePath, heteroJobConfig.getDelay());
            algorithmConfig.setDemeSize(heteroJobConfig.getDemeSize()[i]);
            algorithmConfig.setAcceptanceRuleForOffspring(heteroJobConfig.getAcceptRuleForOffspring()[i]);
            algorithmConfig.setRankingParameter(heteroJobConfig.getRankingParameter()[i]);
            algorithmConfig.setInitStrategy(heteroJobConfig.getInitialSelectionPolicy()[i]);
            algorithmConfig.setAmountFitness(heteroJobConfig.getAmountFitness()[i]);
            algorithmConfig.readFiles(heteroJobConfig.getMinimalHammingDistance()[i], heteroJobConfig.getEvoFileName()[i]);
            algorithmConfigs[i] = algorithmConfig;
        }

        List<List<String>> neighbors = topologyConfig.getNeighbors(heteroJobConfig.getNumberOfIslands(), heteroJobConfig.getTopology());
        Gson gson = new Gson();
        sendToSplittingJoining(gson.toJson(migrationConfigs), "migration");
        sendToSplittingJoining(gson.toJson(algorithmConfigs), "hetero/algorithm");
        sendToSplittingJoining(gson.toJson(neighbors), "hetero/neighbors");
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
            logger.debug("sending hetero config for " + path);
            ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.splittingJoiningURL + "/sjs/config/" + path, entity, String.class);

    }
}
