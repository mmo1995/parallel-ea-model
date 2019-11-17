package iai.kit.edu;

import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class Test {

    public static void main(String[] args) {
     int[] numberOfIslands = new int[]{1};
     int[] populationSizes = new int[]{1024};
     int[] migrationRates = new int[]{4};
     int[] delays = new int[]{0};
     String[] topologies = new String[]{"ring", "biRing","ladder","complete"};

             String epochTerminationCriterion= "generation"; // evaluation ; fitness ;generation
        int epochTerminationEvaluation= 1000000;
        double epochTerminationFitness= 100000.0;
        int epochTerminationGeneration = 1;

        String globalTerminationCriterion = "generation"; // evaluation ; fitness ; generation
        int globalTerminationEpoch = 100; // number of epochs i.e. set to 100
        int globalTerminationEvaluation = 1000000;
        double globalTerminationFitness = 100000;
        int globalTerminationGeneration = 1; // in case of fitness and doesn't find the best solution

        Gson gson = new Gson();
        ExperimentConfig experimentConfig = new ExperimentConfig();
        experimentConfig.setNumberOfIslands(numberOfIslands);
        experimentConfig.setPopulationSize(populationSizes);
        experimentConfig.setMigrationRate(migrationRates);
        experimentConfig.setDelay(delays);
        experimentConfig.setTopology(topologies);

        experimentConfig.setEpochTerminationCriterion(epochTerminationCriterion);
        experimentConfig.setEpochTerminationEvaluation(epochTerminationEvaluation);
        experimentConfig.setEpochTerminationFitness(epochTerminationFitness);
        experimentConfig.setEpochTerminationGeneration(epochTerminationGeneration);

        experimentConfig.setGlobalTerminationCriterion(globalTerminationCriterion);
        experimentConfig.setGlobalTerminationEpoch(globalTerminationEpoch);
        experimentConfig.setGlobalTerminationEvaluation(globalTerminationEvaluation);
        experimentConfig.setGlobalTerminationFitness(globalTerminationFitness);
        experimentConfig.setGlobalTerminationGeneration(globalTerminationGeneration);

        String configurationJson = gson.toJson(experimentConfig);
        RestTemplate restTemplate = new RestTemplate();
        //ResponseEntity<String> answer1 = restTemplate.postForEntity("http://iai-energy1.iai.kit.edu:30188/ojm/start/jobs", configurationJson, String.class);
        ResponseEntity<String> answer1 = restTemplate.postForEntity("http://localhost:8071/ojm/start/jobs", configurationJson, String.class);
       // ResponseEntity<String> answer1 = restTemplate.postForEntity("http://iai-energy1.iai.kit.edu:31671//ojm/start/jobs", configurationJson, String.class);
    }
}
