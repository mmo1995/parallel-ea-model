package iai.kit.edu.controller;

import com.google.gson.Gson;
import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.DynamicJobConfig;
import iai.kit.edu.config.JobConfig;
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Sends the initial population to the splitting and joining service
 */
@CrossOrigin(origins = "*")
@Component
public class PopulationController {
    @Autowired
    private InitializerEAController initializerEAController;
    @Autowired
    private JobConfig jobConfig;
    @Autowired
    private DynamicJobConfig dynamicJobConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void sendPopulation(boolean dynamic) {
        List<String> population;
        if(dynamic){
            population = initializerEAController.initialize(dynamicJobConfig.getGlobalPopulationSize(), dynamicJobConfig.getAmountFitness(), dynamicJobConfig.getInitialSelectionPolicy());

        }else{
            population = initializerEAController.initialize(jobConfig.getGlobalPopulationSize(), jobConfig.getAmountFitness(), jobConfig.getInitialSelectionPolicy());
        }
        //logger.info("initial population: "+ population);
        Gson gson = new Gson();
        sendToSplittingJoiningService(gson.toJson(population));
    }



    private void sendToSplittingJoiningService(String json) {
        //logger.info("initial population: "+ json);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(json, headers);

        logger.info("sending initial population");
        ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.splittingJoiningURL + "/sjs/population/initial", entity, String.class);
    }
}
