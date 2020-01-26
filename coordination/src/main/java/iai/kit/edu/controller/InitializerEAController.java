package iai.kit.edu.controller;

import com.google.gson.Gson;
import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.InitializerEAConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

import java.util.List;


/**
 * Sends request to InitializerEAService to create initial population
 */
public class InitializerEAController {

    RestTemplate restTemplate = new RestTemplate();

    private final Logger logger=LoggerFactory.getLogger(this.getClass());

    /**
     * Sends request to InitializerEAService to create initial population
     * @param populationSize of initial population
     * @param amountFitness of initial population
     * @param initialSelectionPolicy for initial population
     * @return initial population
     */
    public List<String> initialize(int populationSize, double amountFitness, String initialSelectionPolicy){
        InitializerEAConfig initializerEAConfig = new InitializerEAConfig();
        initializerEAConfig.setPopulationSize(populationSize);
        initializerEAConfig.setAmountFitness(amountFitness);
        initializerEAConfig.setInitStrategy(initialSelectionPolicy);
        Gson gson=new Gson();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String initializerEAConfigJson = gson.toJson(initializerEAConfig);
        HttpEntity<String> entity = new HttpEntity<String>(initializerEAConfigJson, headers);
        logger.info("creating initial population");
        ResponseEntity<String> initialPopulationString = restTemplate.postForEntity(ConstantStrings.initializerEAURL +"/ine/population/initial", entity,String.class);
        List<String> initialPopulation = gson.fromJson(initialPopulationString.getBody(),List.class);
        return initialPopulation;
    }

    /**
     * Sends request to InitializerEAService to choose best chromosome from population
     * @param population the population, from which the best chromosome has to be chosen
     * @return the best chromosome has been chosen
     */
    public String chooseBestChromosome(List<String> population){

        ResponseEntity<String> bestChromosomeString = restTemplate.postForEntity(ConstantStrings.initializerEAURL +"/chromosomes/best", population,String.class);
        return bestChromosomeString.getBody();
    }
}
