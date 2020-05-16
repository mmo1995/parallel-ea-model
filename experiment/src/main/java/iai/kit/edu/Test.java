package iai.kit.edu;

import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class Test {

    private static void runExperiment(){
        int[] numberOfIslands = new int[]{3};
        int[] numberOfSlaves = new int[] {1};
        int[] populationSizes = new int[]{200}; //240 for each island
        int[] migrationRates = new int[]{1};
        int[] generationAmount = new int[]{3};
        int[] delays = new int[]{0};
        //String[] topologies = new String[]{"ring", "biRing","ladder","complete"};
        String[] topologies = new String[]{"ring"};

        int[] demeSize = new int[]{8};

        String epochTerminationCriterion= "generation"; // evaluation ; fitness ;generation
        int epochTerminationEvaluation= 1000000;
        double epochTerminationFitness= 100000;
        int epochTerminationGeneration = 4; // number of evolution inside the each island i.e. nr. of generation of Master-slave

        String globalTerminationCriterion = "generation"; // evaluation ; fitness ; generation
        int globalTerminationEpoch = 3; // number of epochs i.e. set to 100
        int globalTerminationEvaluation = 1000000;
        double globalTerminationFitness = 85000;
        int globalTerminationGeneration = 1000; // the max number of generation if we use Fitness as a termination criterium
        Gson gson = new Gson();
        ExperimentConfig experimentConfig = new ExperimentConfig();
        experimentConfig.setNumberOfIslands(numberOfIslands);
        experimentConfig.setNumberOfSlaves(numberOfSlaves);
        experimentConfig.setPopulationSize(populationSizes);
        experimentConfig.setMigrationRate(migrationRates);
        experimentConfig.setDelay(delays);
        experimentConfig.setDemeSize(demeSize);
        experimentConfig.setTopology(topologies);
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
        ResponseEntity<String> answer1 = restTemplate.postForEntity("http://iai-energy1.iai.kit.edu:30004/ojm/start/jobs", configurationJson, String.class);
        //ResponseEntity<String> answer1 = restTemplate.postForEntity("http://localhost:8071/ojm/start/jobs", configurationJson, String.class);
        // ResponseEntity<String> answer1 = restTemplate.postForEntity("http://iai-energy1.iai.kit.edu:31671//ojm/start/jobs", configurationJson, String.class);
    }

    private static void runDynamic(){ //all dynamic arrays must have the same length as numberOfIslands
        int numberOfIslands = 3;
        int numberOfSlaves = 1;
        int populationSize = 200;
        int[] migrationRates = new int[]{4,5,6};
        int[] generationAmount = new int[]{3,3,3};
        String[] selectionPolicy= {"best", "best","best"};
        String[] replacementPolicy= {"worst", "worst", "worst"};

        int delay = 0;
        //String[] topologies = new String[]{"ring", "biRing","ladder","complete"};
        String topology = "ring";

        String[] epochTerminationCriterion= new String[] {"generation", "generation", "generation"}; // evaluation ; fitness ;generation
        int[] epochTerminationEvaluation= new int[] {1000000,1000000,1000000};
        double[] epochTerminationFitness= new double[] {30000, 35000, 30000};
        int[] epochTerminationGeneration = new int[] {3,3,10}; // number of evolution inside the each island i.e. nr. of generation of Master-slave
        int[] epochTerminationTime = new int[] {5,7,8};
        int[] epochTerminationGDV = new int[] {500,600,700};
        int[] epochTerminationGAK = new int[] {100,150,160};

        int[] demeSize = new int[] {8,8,8};


        String globalTerminationCriterion = "generation"; // evaluation ; fitness ; generation
        int globalTerminationEpoch = 3; // number of epochs i.e. set to 100
        int globalTerminationEvaluation = 1000000;
        double globalTerminationFitness = 85000;
        int globalTerminationGeneration = 1000; // the max number of generation if we use Fitness as a termination criterium
        Gson gson = new Gson();
        DynamicConfiguration dynamicConfiguration = new DynamicConfiguration();
        dynamicConfiguration.setNumberOfIslands(numberOfIslands);
        dynamicConfiguration.setNumberOfSlaves(numberOfSlaves);
        dynamicConfiguration.setGlobalPopulationSize(populationSize);
        dynamicConfiguration.setMigrationRate(migrationRates);
        dynamicConfiguration.setDelay(delay);
        dynamicConfiguration.setSelectionPolicy(selectionPolicy);
        dynamicConfiguration.setReplacementPolicy(replacementPolicy);
        dynamicConfiguration.setTopology(topology);
        dynamicConfiguration.setNumberOfGeneration(generationAmount);
        dynamicConfiguration.setDemeSize(demeSize);

        dynamicConfiguration.setEpochTerminationCriterion(epochTerminationCriterion);
        dynamicConfiguration.setEpochTerminationEvaluation(epochTerminationEvaluation);
        dynamicConfiguration.setEpochTerminationFitness(epochTerminationFitness);
        dynamicConfiguration.setEpochTerminationGeneration(epochTerminationGeneration);
        dynamicConfiguration.setEpochTerminationTime(epochTerminationTime);
        dynamicConfiguration.setEpochTerminationGDV(epochTerminationGDV);
        dynamicConfiguration.setEpochTerminationGAK(epochTerminationGAK);

        dynamicConfiguration.setGlobalTerminationCriterion(globalTerminationCriterion);
        dynamicConfiguration.setGlobalTerminationEpoch(globalTerminationEpoch);
        dynamicConfiguration.setGlobalTerminationEvaluation(globalTerminationEvaluation);
        dynamicConfiguration.setGlobalTerminationFitness(globalTerminationFitness);
        dynamicConfiguration.setGlobalTerminationGeneration(globalTerminationGeneration);

        String configurationJson = gson.toJson(dynamicConfiguration);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> answer1 = restTemplate.postForEntity("http://iai-energy1.iai.kit.edu:30004/ojm/start/job/dynamic", configurationJson, String.class);
        //ResponseEntity<String> answer1 = restTemplate.postForEntity("http://localhost:8071/ojm/start/job/dynamic", configurationJson, String.class);
        // ResponseEntity<String> answer1 = restTemplate.postForEntity("http://iai-energy1.iai.kit.edu:31671/start/job/dynamic", configurationJson, String.class);
    }

    public static void main(String[] args) {
     runExperiment();
     //runDynamic();
    }
}
