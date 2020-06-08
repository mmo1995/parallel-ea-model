package iai.kit.edu;

import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class Test {

    private static void runExperiment(){
        int[] numberOfIslands = new int[]{1};
        int[] numberOfSlaves = new int[] {1};
        int[] populationSizes = new int[]{10}; //240 for each island
        int[] migrationRates = new int[]{1};
        int[] generationAmount = new int[]{3};
        int[] delays = new int[]{0};
        //String[] topologies = new String[]{"ring", "biRing","ladder","complete"};
        String[] topologies = new String[]{"ring"};

        int[] demeSize = new int[]{6};
        String[] acceptRuleForOffspring = {"localLeast-ES"}; //always, localLeast, betterParent
        double[] rankingParameter = {1.45};

        String[] initialSelectionPolicyInitializer= {"new"};
        int[] amountFitnessInitializer = {0};

        String[] initialSelectionPolicy= {"bestNew"};
        int[] amountFitness = {1};

        boolean[] asyncMigration = {true};

        double[] minimalHammingDistance = {0.2};

        String epochTerminationCriterion= "generation"; // evaluation ; fitness ;generation
        int epochTerminationEvaluation= 1000000;
        double epochTerminationFitness= 100000;
        int epochTerminationGeneration = 1; // number of evolution inside the each island i.e. nr. of generation of Master-slave

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
        experimentConfig.setAcceptRuleForOffspring(acceptRuleForOffspring);
        experimentConfig.setRankingParameter(rankingParameter);
        experimentConfig.setMinimalHammingDistance(minimalHammingDistance);
        experimentConfig.setInitialSelectionPolicy(initialSelectionPolicy);
        experimentConfig.setAmountFitness(amountFitness);
        experimentConfig.setInitialSelectionPolicyInitializer(initialSelectionPolicyInitializer);
        experimentConfig.setAmountFitnessInitializer(amountFitnessInitializer);

        experimentConfig.setEpochTerminationCriterion(epochTerminationCriterion);
        experimentConfig.setEpochTerminationEvaluation(epochTerminationEvaluation);
        experimentConfig.setEpochTerminationFitness(epochTerminationFitness);
        experimentConfig.setEpochTerminationGeneration(epochTerminationGeneration);

        experimentConfig.setGlobalTerminationCriterion(globalTerminationCriterion);
        experimentConfig.setGlobalTerminationEpoch(globalTerminationEpoch);
        experimentConfig.setGlobalTerminationEvaluation(globalTerminationEvaluation);
        experimentConfig.setGlobalTerminationFitness(globalTerminationFitness);
        experimentConfig.setGlobalTerminationGeneration(globalTerminationGeneration);

        experimentConfig.setAsyncMigration(asyncMigration);

        String configurationJson = gson.toJson(experimentConfig);
        RestTemplate restTemplate = new RestTemplate();
        //ResponseEntity<String> answer1 = restTemplate.postForEntity("http://iai-energy1.iai.kit.edu:30004/ojm/start/jobs", configurationJson, String.class);
        ResponseEntity<String> answer1 = restTemplate.postForEntity("http://localhost:8071/ojm/start/jobs", configurationJson, String.class);
        // ResponseEntity<String> answer1 = restTemplate.postForEntity("http://iai-energy1.iai.kit.edu:31671//ojm/start/jobs", configurationJson, String.class);
    }

    private static void runHetero(){ //all heterogeneous arrays must have the same length as numberOfIslands
        int numberOfIslands = 2;
        int numberOfSlaves = 1;
        int populationSize = 20;
        int[] migrationRates = new int[]{4,5};
        int[] generationAmount = new int[]{3,3};
        String[] selectionPolicy= {"best","best"};
        String[] replacementPolicy= {"worst","worst"};

        String initialSelectionPolicyInitializer = "best";
        int amountFitnessInitializer = 0;

        String[] initialSelectionPolicy= {"new", "mix"};
        int[] amountFitness= {2,1};

        int delay = 0;
        //String[] topologies = new String[]{"ring", "biRing","ladder","complete"};
        String topology = "ring";

        boolean asyncMigration = true;

        String[] epochTerminationCriterion= new String[] {"generation","generation"}; // evaluation ; fitness ;generation
        int[] epochTerminationEvaluation= new int[] {1000000,1000000};
        double[] epochTerminationFitness= new double[] {30000,30000};
        int[] epochTerminationGeneration = new int[] {2,2}; // number of evolution inside the each island i.e. nr. of generation of Master-slave
        int[] epochTerminationTime = new int[] {5,3};
        int[] epochTerminationGDV = new int[] {500,500};
        int[] epochTerminationGAK = new int[] {100,100};

        int[] demeSize = new int[] {8,8};
        String[] acceptRuleForOffspring = {"always","localLeast"}; //localLeast-ES, always, localLeast, betterParent
        double[] rankingParameter = {1.46, 1.50};
        double[] minimalHammingDistance = {0.3,0.2};


        String globalTerminationCriterion = "generation"; // evaluation ; fitness ; generation
        int globalTerminationEpoch = 3; // number of epochs i.e. set to 100
        int globalTerminationEvaluation = 1000000;
        double globalTerminationFitness = 60000;
        int globalTerminationGeneration = 1000; // the max number of generation if we use Fitness as a termination criterium
        Gson gson = new Gson();
        HeteroConfiguration heteroConfiguration = new HeteroConfiguration();
        heteroConfiguration.setNumberOfIslands(numberOfIslands);
        heteroConfiguration.setNumberOfSlaves(numberOfSlaves);
        heteroConfiguration.setGlobalPopulationSize(populationSize);
        heteroConfiguration.setMigrationRate(migrationRates);
        heteroConfiguration.setDelay(delay);
        heteroConfiguration.setSelectionPolicy(selectionPolicy);
        heteroConfiguration.setReplacementPolicy(replacementPolicy);
        heteroConfiguration.setTopology(topology);
        heteroConfiguration.setNumberOfGeneration(generationAmount);
        heteroConfiguration.setDemeSize(demeSize);
        heteroConfiguration.setAcceptRuleForOffspring(acceptRuleForOffspring);
        heteroConfiguration.setRankingParameter(rankingParameter);
        heteroConfiguration.setMinimalHammingDistance(minimalHammingDistance);
        heteroConfiguration.setInitialSelectionPolicy(initialSelectionPolicy);
        heteroConfiguration.setAmountFitness(amountFitness);
        heteroConfiguration.setInitialSelectionPolicyInitializer(initialSelectionPolicyInitializer);
        heteroConfiguration.setAmountFitnessInitializer(amountFitnessInitializer);
        heteroConfiguration.setAsyncMigration(asyncMigration);

        heteroConfiguration.setEpochTerminationCriterion(epochTerminationCriterion);
        heteroConfiguration.setEpochTerminationEvaluation(epochTerminationEvaluation);
        heteroConfiguration.setEpochTerminationFitness(epochTerminationFitness);
        heteroConfiguration.setEpochTerminationGeneration(epochTerminationGeneration);
        heteroConfiguration.setEpochTerminationTime(epochTerminationTime);
        heteroConfiguration.setEpochTerminationGDV(epochTerminationGDV);
        heteroConfiguration.setEpochTerminationGAK(epochTerminationGAK);

        heteroConfiguration.setGlobalTerminationCriterion(globalTerminationCriterion);
        heteroConfiguration.setGlobalTerminationEpoch(globalTerminationEpoch);
        heteroConfiguration.setGlobalTerminationEvaluation(globalTerminationEvaluation);
        heteroConfiguration.setGlobalTerminationFitness(globalTerminationFitness);
        heteroConfiguration.setGlobalTerminationGeneration(globalTerminationGeneration);

        String configurationJson = gson.toJson(heteroConfiguration);
        RestTemplate restTemplate = new RestTemplate();
        //ResponseEntity<String> answer1 = restTemplate.postForEntity("http://iai-energy1.iai.kit.edu:30004/ojm/start/job/hetero", configurationJson, String.class);
        ResponseEntity<String> answer1 = restTemplate.postForEntity("http://localhost:8071/ojm/start/job/hetero", configurationJson, String.class);
        // ResponseEntity<String> answer1 = restTemplate.postForEntity("http://iai-energy1.iai.kit.edu:31671/start/job/hetero", configurationJson, String.class);
    }

    private static void runHeteroExperiment(){
        int[] numberOfIslands = {2};
        int[] numberOfSlaves = {1};
        int[] populationSize = {20};
        int[][] migrationRates = new int[][]{{4,5}};
        int[][] generationAmount = new int[][]{{3,3}};
        String[][] selectionPolicy= {{"best","best"}};
        String[][] replacementPolicy= {{"worst","worst"}};

        String[] initialSelectionPolicyInitializer = {"best"};
        int[] amountFitnessInitializer = {0};

        String[][] initialSelectionPolicy= {{"new", "mix"}};
        int[][] amountFitness= {{2,1}};

        int[] delay = {0};
        //String[] topologies = new String[]{"ring", "biRing","ladder","complete"};
        String[] topology = {"ring"};

        boolean[] asyncMigration = {true};

        String[][] epochTerminationCriterion= new String[][] {{"generation","generation"}}; // evaluation ; fitness ;generation
        int[][] epochTerminationEvaluation= new int[][] {{1000000,1000000}};
        double[][] epochTerminationFitness= new double[][] {{30000,30000}};
        int[][] epochTerminationGeneration = new int[][] {{2,2}}; // number of evolution inside the each island i.e. nr. of generation of Master-slave
        int[][] epochTerminationTime = new int[][] {{5,3}};
        int[][] epochTerminationGDV = new int[][] {{500,500}};
        int[][] epochTerminationGAK = new int[][] {{100,100}};

        int[][] demeSize = new int[][] {{8,8}};
        String[][] acceptRuleForOffspring = {{"always","localLeast"}}; //localLeast-ES, always, localLeast, betterParent
        double[][] rankingParameter = {{1.46, 1.50}};
        double[][] minimalHammingDistance = {{0.3,0.2}};


        String[] globalTerminationCriterion = {"generation"}; // evaluation ; fitness ; generation
        int[] globalTerminationEpoch = {3}; // number of epochs i.e. set to 100
        int[] globalTerminationEvaluation = {1000000};
        double[] globalTerminationFitness = {60000};
        int[] globalTerminationGeneration = {1000}; // the max number of generation if we use Fitness as a termination criterium
        Gson gson = new Gson();
        HeteroExperimentConfig heteroExperimentConfig = new HeteroExperimentConfig();
        heteroExperimentConfig.setNumberOfIslands(numberOfIslands);
        heteroExperimentConfig.setNumberOfSlaves(numberOfSlaves);
        heteroExperimentConfig.setGlobalPopulationSize(populationSize);
        heteroExperimentConfig.setMigrationRate(migrationRates);
        heteroExperimentConfig.setDelay(delay);
        heteroExperimentConfig.setSelectionPolicy(selectionPolicy);
        heteroExperimentConfig.setReplacementPolicy(replacementPolicy);
        heteroExperimentConfig.setTopology(topology);
        heteroExperimentConfig.setNumberOfGeneration(generationAmount);
        heteroExperimentConfig.setDemeSize(demeSize);
        heteroExperimentConfig.setAcceptRuleForOffspring(acceptRuleForOffspring);
        heteroExperimentConfig.setRankingParameter(rankingParameter);
        heteroExperimentConfig.setMinimalHammingDistance(minimalHammingDistance);
        heteroExperimentConfig.setInitialSelectionPolicy(initialSelectionPolicy);
        heteroExperimentConfig.setAmountFitness(amountFitness);
        heteroExperimentConfig.setInitialSelectionPolicyInitializer(initialSelectionPolicyInitializer);
        heteroExperimentConfig.setAmountFitnessInitializer(amountFitnessInitializer);
        heteroExperimentConfig.setAsyncMigration(asyncMigration);

        heteroExperimentConfig.setEpochTerminationCriterion(epochTerminationCriterion);
        heteroExperimentConfig.setEpochTerminationEvaluation(epochTerminationEvaluation);
        heteroExperimentConfig.setEpochTerminationFitness(epochTerminationFitness);
        heteroExperimentConfig.setEpochTerminationGeneration(epochTerminationGeneration);
        heteroExperimentConfig.setEpochTerminationTime(epochTerminationTime);
        heteroExperimentConfig.setEpochTerminationGDV(epochTerminationGDV);
        heteroExperimentConfig.setEpochTerminationGAK(epochTerminationGAK);

        heteroExperimentConfig.setGlobalTerminationCriterion(globalTerminationCriterion);
        heteroExperimentConfig.setGlobalTerminationEpoch(globalTerminationEpoch);
        heteroExperimentConfig.setGlobalTerminationEvaluation(globalTerminationEvaluation);
        heteroExperimentConfig.setGlobalTerminationFitness(globalTerminationFitness);
        heteroExperimentConfig.setGlobalTerminationGeneration(globalTerminationGeneration);

        String configurationJson = gson.toJson(heteroExperimentConfig);
        RestTemplate restTemplate = new RestTemplate();
        //ResponseEntity<String> answer1 = restTemplate.postForEntity("http://iai-energy1.iai.kit.edu:30004/ojm/start/jobs/hetero", configurationJson, String.class);
        ResponseEntity<String> answer1 = restTemplate.postForEntity("http://localhost:8071/ojm/start/jobs/hetero", configurationJson, String.class);
        // ResponseEntity<String> answer1 = restTemplate.postForEntity("http://iai-energy1.iai.kit.edu:31671/start/jobs/hetero", configurationJson, String.class);
    }

    public static void main(String[] args) {
     //runExperiment();
     //runHetero();
        runHeteroExperiment();
    }
}
