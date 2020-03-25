package iai.kit.edu;

import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class Test {

    public static void main(String[] args) {
     int[] numberOfIslands = new int[]{1};
     int[] numberOfSlaves = new int[] {1};
     int[] populationSizes = new int[]{10};
     int[] migrationRates = new int[]{1};
     int[] generationAmount = new int[]{1};
     int[] delays = new int[]{0};
     //String[] topologies = new String[]{"ring", "biRing","ladder","complete"};
        String[] topologies = new String[]{"ring"};

     String date = "2013-01-01";
        String epochTerminationCriterion= "generation"; // evaluation ; fitness ;generation
        int epochTerminationEvaluation= 1000000;
        double epochTerminationFitness= 100000.0;
        int epochTerminationGeneration = 1; // number of evolution inside the each island i.e. nr. of generation of Master-slave

        String globalTerminationCriterion = "generation"; // evaluation ; fitness ; generation
        int globalTerminationEpoch = 10; // number of epochs i.e. set to 100
        int globalTerminationEvaluation = 1000000;
        double globalTerminationFitness = 100000;
        int globalTerminationGeneration = 1; // the max number of generation if we use Fitness as a termination criterium
        Gson gson = new Gson();
        ExperimentConfig experimentConfig = new ExperimentConfig();
        experimentConfig.setNumberOfIslands(numberOfIslands);
        experimentConfig.setNumberOfSlaves(numberOfSlaves);
        experimentConfig.setPopulationSize(populationSizes);
        experimentConfig.setMigrationRate(migrationRates);
        experimentConfig.setDelay(delays);
        experimentConfig.setTopology(topologies);
        experimentConfig.setDate(date);
        experimentConfig.setNumberOfGeneration(generationAmount);


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
           // ResponseEntity<String> answer1 = restTemplate.postForEntity("http://iai-energy1.iai.kit.edu:30004/ojm/start/jobs", configurationJson, String.class);
            ResponseEntity<String> answer1 = restTemplate.postForEntity("http://localhost:8071/ojm/start/jobs", configurationJson, String.class);
       // ResponseEntity<String> answer1 = restTemplate.postForEntity("http://iai-energy1.iai.kit.edu:31671//ojm/start/jobs", configurationJson, String.class);
    }
}
