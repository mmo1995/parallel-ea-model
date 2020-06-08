package iai.kit.edu.controller;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import iai.kit.edu.config.*;
import iai.kit.edu.core.AlgorithmManager;
import iai.kit.edu.core.Overhead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Controls start and receiving the result
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ojm")
public class AlgorithmController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private long endTime = 0;
    private int jobId = 0;
    @Autowired
    private AlgorithmManager algorithmManager;

    @Autowired
    private InitializerEAController initializerEAController;
    @Autowired
    private Autowiring redisConnection;
    @Autowired
    private JobConfig jobConfig;

    @Autowired
    private HeteroJobConfig heteroJobConfig;

    private static RestTemplate restTemplate = new RestTemplate();

    private List<String> resultsCollection;

    private List<JobConfig> jobConfigList;

    private boolean experiment;

    private boolean hetero;

    private RedisAtomicInteger amountOfGeneration;

    private JedisConnectionFactory jedisConnectionFactory;
    private RedisConnection jRedisconn;
    private int numberOfMigrationsFitness = 0;
    private Map<Integer, Double> executionTimeEAs = new HashMap<>();
    private Map<Integer, Double> migrationsOverheads = new HashMap<>();
    @Autowired
    private Overhead overhead;

    @Autowired
    @Qualifier("integerTemplate")
    RedisTemplate<String, Integer> template;

    /**
     * Receive configuration for one optimization task
     * @param json of configuration
     */
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public void receiveStartConfiguration(@RequestBody String json) {
        experiment = false;
        hetero = false;
        jobConfig.readFromJson(json);
        logger.info("received job config: " + jobConfig.toString());
        overhead.setStartEvolution(System.currentTimeMillis());
        overhead.setStartInitializationOverhead(System.currentTimeMillis());
        algorithmManager.initialize(false);
    }

    /**
     * Receive configuration for several optimization task
     * @param json of configurations
     */
    @RequestMapping(value = "/start/jobs", method = RequestMethod.POST)
    public void receiveStartConfigurations(@RequestBody String json) {
        amountOfGeneration = new RedisAtomicInteger(ConstantStrings.gleamConfigurationsGeneration, template.getConnectionFactory());
        experiment = true;
        hetero = false;
        Gson gson = new Gson();
        resultsCollection = new ArrayList<>();
        ExperimentConfig experimentConfig = gson.fromJson(json, ExperimentConfig.class);
        int[] numberOfIslands = experimentConfig.getNumberOfIslands();
        int[] numberOfSlaves = experimentConfig.getNumberOfSlaves();
        int[] populationSizes = experimentConfig.getPopulationSize();
        int[] delays = experimentConfig.getDelay();
        int[] demeSizes = experimentConfig.getDemeSize();
        String[] acceptanceRulesForOffspring = experimentConfig.getAcceptRuleForOffspring();
        double[] rankingParameters = experimentConfig.getRankingParameter();
        String[] initialSelectionPolicy = experimentConfig.getInitialSelectionPolicy();
        int[] amountOfFitness = experimentConfig.getAmountOfFitness();

        boolean[] asyncMigration = experimentConfig.getAsyncMigration();

        String[] initialSelectionPolicyInitializer = experimentConfig.getInitialSelectionPolicyInitializer();
        int[] amountOfFitnessInitializer = experimentConfig.getAmountFitnessInitializer();

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
        int[] numberOfGeneration = experimentConfig.getNumberOfGeneration();
        double[] minimalHammingDistance = experimentConfig.getMinimalHammingDistance();

        jobConfigList = new ArrayList<>();
        for (int i = 0; i < numberOfIslands.length; i++) {
            for (int ii = 0; ii < numberOfSlaves.length; ii++) {
                for (int j = 0; j < populationSizes.length; j++) {
                    for (int jj = 0; jj < numberOfGeneration.length; jj++) {
                        for (int k = 0; k < delays.length; k++) {
                            for (int l = 0; l < migrationRates.length; l++) {
                                for (int m = 0; m < topologies.length; m++) {
                                    for(int n = 0; n< demeSizes.length; n++){
                                        for(int o = 0; o< acceptanceRulesForOffspring.length; o++){
                                            for(int p = 0; p<rankingParameters.length; p++){
                                                for(int q = 0; q<minimalHammingDistance.length;q++){
                                                    for(int r = 0; r<initialSelectionPolicy.length;r++){
                                                        for(int s = 0; s<amountOfFitness.length; s++){
                                                            for(int t = 0; t<initialSelectionPolicyInitializer.length; t++){
                                                                for(int u = 0; u< amountOfFitnessInitializer.length; u++){
                                                                    for(int v = 0; v<asyncMigration.length; v++){
                                                                        JobConfig jobConfigTemp = new JobConfig();
                                                                        jobConfigTemp.setNumberOfIslands(numberOfIslands[i]);
                                                                        jobConfigTemp.setNumberOfSlaves(numberOfSlaves[ii]);
                                                                        jobConfigTemp.setGlobalPopulationSize(populationSizes[j]);
                                                                        jobConfigTemp.setNumberOfGeneration(numberOfGeneration[jj]);
                                                                        jobConfigTemp.setDelay(delays[k]);
                                                                        jobConfigTemp.setMigrationRate(migrationRates[l]);
                                                                        jobConfigTemp.setTopology(topologies[m]);
                                                                        jobConfigTemp.setDemeSize(demeSizes[n]);
                                                                        jobConfigTemp.setEpochTerminationCriterion(epochTerminationCriterion_1);
                                                                        jobConfigTemp.setEpochTerminationGeneration(epochTerminationGeneration_1);
                                                                        jobConfigTemp.setEpochTerminationEvaluation(epochTerminationEvaluation_1);
                                                                        jobConfigTemp.setEpochTerminationFitness(epochTerminationFitness_1);
                                                                        jobConfigTemp.setGlobalTerminationCriterion(globalTerminationCriterion_1);
                                                                        jobConfigTemp.setGlobalTerminationEpoch(globalTerminationEpoch_1);
                                                                        jobConfigTemp.setGlobalTerminationEvaluation(globalTerminationEvaluation_1);
                                                                        jobConfigTemp.setGlobalTerminationGeneration(globalTerminationGeneration_1);
                                                                        jobConfigTemp.setGlobalTerminationFitness(globalTerminationFitness_1);
                                                                        jobConfigTemp.setAcceptRuleForOffspring(acceptanceRulesForOffspring[o]);
                                                                        jobConfigTemp.setRankingParameter(rankingParameters[p]);
                                                                        jobConfigTemp.setMinimalHammingDistance(minimalHammingDistance[q]);
                                                                        jobConfigTemp.setInitialSelectionPolicy(initialSelectionPolicy[r]);
                                                                        jobConfigTemp.setAmountFitness(amountOfFitness[s]);
                                                                        jobConfigTemp.setInitialSelectionPolicyInitializer(initialSelectionPolicyInitializer[t]);
                                                                        jobConfigTemp.setAmountFitnessInitializer(amountOfFitnessInitializer[u]);
                                                                        jobConfigTemp.setAsyncMigration(asyncMigration[v]);
                                                                        jobConfigList.add(jobConfigTemp);
                                                                        // speedupresults.setJobConfig(jobConfig);
                                                                        // speedUpResultsList.add(speedupresults);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        /*jedisConnectionFactory = redisConnection.returnJedisConnectionFactory();
        jRedisconn= jedisConnectionFactory.getConnection();
        jRedisconn.flushAll();*/
        jobConfig.readFromExistingJobConfig(jobConfigList.remove(0));
        logger.info("received job config: " + jobConfig.toString());
        amountOfGeneration.set(jobConfig.getEpochTerminationGeneration()+1);
        overhead.setStartEvolution(System.currentTimeMillis());
        overhead.setStartInitializationOverhead(System.currentTimeMillis());
        algorithmManager.initialize(false);
    }

    /**
     * Receive configuration for one heterogeneous job
     * @param json of configurations
     */
    @RequestMapping(value = "/start/job/hetero", method = RequestMethod.POST)
    public void receiveHeteroStartConfiguration(@RequestBody String json) {
        experiment = false;
        hetero = true;
        amountOfGeneration = new RedisAtomicInteger(ConstantStrings.gleamConfigurationsGeneration, template.getConnectionFactory());
        resultsCollection = new ArrayList<>();
        heteroJobConfig.readFromJson(json);
        //amountOfGeneration.set(heteroJobConfig.getEpochTerminationGeneration()+1);
        logger.info("received heterogeneous job config: " + heteroJobConfig.toString());
        overhead.setStartEvolution(System.currentTimeMillis());
        overhead.setStartInitializationOverhead(System.currentTimeMillis());
        algorithmManager.initialize(true);
    }

    /**
     * Receive configuration for several heterogeneous jobs
     * @param json of configurations
     */
    @RequestMapping(value = "/start/jobs/hetero", method = RequestMethod.POST)
    public void receiveHeteroStartConfigurations(@RequestBody String json) {
        experiment = true;
        hetero = true;
        amountOfGeneration = new RedisAtomicInteger(ConstantStrings.gleamConfigurationsGeneration, template.getConnectionFactory());
        resultsCollection = new ArrayList<>();
        heteroJobConfig.readFromJson(json);
        //amountOfGeneration.set(heteroJobConfig.getEpochTerminationGeneration()+1);
        logger.info("received heterogeneous job config: " + heteroJobConfig.toString());
        overhead.setStartEvolution(System.currentTimeMillis());
        overhead.setStartInitializationOverhead(System.currentTimeMillis());
        algorithmManager.initialize(true);
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
        String bestChromosome = initializerEAController.chooseBestChromosome(result);
        String formattedChromosome = formatBestChromosome(bestChromosome);
        sendFormattedChromosome(formattedChromosome);
    }


    @RequestMapping(value = "/{islandNumber}/{sumEAExecution}/executiontime", method = RequestMethod.POST)
    public synchronized void  receiveExecutionTime(@PathVariable String islandNumber, @PathVariable String sumEAExecution, @RequestBody String migrationOverhead) {
        String migrationsOverhead = migrationOverhead.substring(0,migrationOverhead.indexOf("#"));
        String numberOfMigrations = migrationOverhead.substring(migrationOverhead.indexOf("#")+1);

        executionTimeEAs.put(Integer.parseInt(islandNumber), Double.parseDouble(sumEAExecution));
        migrationsOverheads.put(Integer.parseInt(islandNumber), Double.parseDouble(migrationsOverhead));
        numberOfMigrationsFitness = Integer.parseInt(numberOfMigrations);

    }
    public double returnMaxEAExecutionTime ()
    {

        return Collections.max(executionTimeEAs.values());
    }
    public double returnMinEAExecutionTime ()
    {

        return Collections.min(executionTimeEAs.values());

    }
    public double returnAverageEAExecutionTime ()
    {
        double sum = 0.0;
        for(double d: executionTimeEAs.values()){
            sum += d;
        }
        return sum / (double) executionTimeEAs.size();
    }
    public double returnMigrationOverheadMax()
    {
        return Collections.max(migrationsOverheads.values());
    }
    public double returnMigrationOverheadMin()
    {
        return Collections.min(migrationsOverheads.values());
    }
    public double returnMigrationOverheadAverage()
    {
        double sum = 0.0;
        for(double d: migrationsOverheads.values()){
            sum += d;
        }
        return sum / (double) migrationsOverheads.size();
    }
    /**
     * Receive final result
     * @param constraintsAndresultJson of final result
     */
    @RequestMapping(value = "/finalResult", method = RequestMethod.POST)
    public void receiveFinalResult(@RequestBody String constraintsAndresultJson) {

        overhead.setEndEvolution(System.currentTimeMillis());
        logger.info("receiving final result");
        logger.info("Evolution duration " + TimeUnit.MILLISECONDS.toSeconds(overhead.getEndEvolution() - overhead.getStartEvolution()));
        double overallExecutiontime= TimeUnit.MILLISECONDS.toSeconds(overhead.getEndEvolution() - overhead.getStartEvolution());
        double durationIslandsCreation= TimeUnit.MILLISECONDS.toSeconds(overhead.getEndIslandCreation() - overhead.getStartIslandCreation());
        double durationSlavesCreation = TimeUnit.MILLISECONDS.toSeconds(overhead.getEndSlaveCreation() - overhead.getStartSlaveCreation());
        double frameworkOverhead = overallExecutiontime - returnMaxEAExecutionTime();
        double initializationOverhead = TimeUnit.MILLISECONDS.toSeconds(overhead.getEndInitializationOverhead() - overhead.getStartInitializationOverhead());


        String  constraintResults = constraintsAndresultJson.substring(0, constraintsAndresultJson.indexOf("#"));
        String resultJson = constraintsAndresultJson.substring(constraintsAndresultJson.indexOf("#")+1, constraintsAndresultJson.length());
        String [] splitconstraintResults = constraintResults.split("\n");
        String [] constraintResultsValues = splitconstraintResults[0].split(" ");

        jobId ++;
        logger.info("received the results of the optimal scheduling plan #"+ splitconstraintResults[0] + "#");
        logger.info("#####");

        JsonObject dataToVisualizeObject = new JsonObject();
        JsonArray dataToVisualizeArray = new JsonArray();
        JsonObject durationDataObject = new JsonObject();
        Gson gson = new Gson();
        //String jsonInString = gson.toJson(resultJson);
        JsonObject finalSchPlan = new JsonParser().parse(resultJson).getAsJsonObject();

        dataToVisualizeArray.add(finalSchPlan);
        dataToVisualizeObject.addProperty("JobID", jobId);
        if(!hetero){
            JsonObject configuration = new JsonObject();
            configuration.addProperty("Hetero", false);
            configuration.addProperty("NumberOfIslands",jobConfig.getNumberOfIslands());
            configuration.addProperty("NumberOfSlaves",jobConfig.getNumberOfSlaves());
            configuration.addProperty("PopulationSize",jobConfig.getGlobalPopulationSize());
            configuration.addProperty("Generation number",jobConfig.getEpochTerminationGeneration());
            configuration.addProperty("Topology",jobConfig.getTopology());
            configuration.addProperty("Migration Rate",jobConfig.getMigrationRate());
            configuration.addProperty("Delay",jobConfig.getDelay());
            configuration.addProperty("Deme Size",jobConfig.getDemeSize());
            configuration.addProperty("Acceptance Rule for Offspring",jobConfig.getAcceptRuleForOffspring());
            configuration.addProperty("Ranking Parameter",jobConfig.getRankingParameter());
            configuration.addProperty("Async Migration",jobConfig.isAsyncMigration());
            configuration.addProperty("Minimal Hamming Distance",jobConfig.getMinimalHammingDistance());
            dataToVisualizeObject.add("Job Configuration", configuration);
        } else {

            JsonArray configuration = new JsonArray();
            JsonObject generalConfiguration = new JsonObject();
            generalConfiguration.addProperty("Hetero", true);
            generalConfiguration.addProperty("NumberOfIslands",heteroJobConfig.getNumberOfIslands());
            generalConfiguration.addProperty("NumberOfSlaves",heteroJobConfig.getNumberOfSlaves());
            generalConfiguration.addProperty("PopulationSize",heteroJobConfig.getGlobalPopulationSize());
            generalConfiguration.addProperty("Topology",heteroJobConfig.getTopology());
            generalConfiguration.addProperty("Delay",heteroJobConfig.getDelay());
            generalConfiguration.addProperty("Async Migration",heteroJobConfig.isAsyncMigration());
            configuration.add(generalConfiguration);
            for(int i = 0; i< heteroJobConfig.getNumberOfIslands(); i++){
                JsonObject islandObject = new JsonObject();
                islandObject.addProperty("Island number", i+1);
                islandObject.addProperty("Generation number",heteroJobConfig.getEpochTerminationGeneration()[i]);
                islandObject.addProperty("Migration Rate",heteroJobConfig.getMigrationRate()[i]);
                islandObject.addProperty("Deme Size",heteroJobConfig.getDemeSize()[i]);
                islandObject.addProperty("Acceptance Rule for Offspring",heteroJobConfig.getAcceptRuleForOffspring()[i]);
                islandObject.addProperty("Ranking Parameter",heteroJobConfig.getRankingParameter()[i]);
                islandObject.addProperty("Minimal Hamming Distance",heteroJobConfig.getMinimalHammingDistance()[i]);
                configuration.add(islandObject);
            }
            dataToVisualizeObject.add("Job Configuration",configuration);
        }
        durationDataObject.addProperty("Overall Execution time",overallExecutiontime );
        durationDataObject.addProperty("DurationEAExecutionMax",returnMaxEAExecutionTime());
        durationDataObject.addProperty("DurationEAExecutionAverage",returnAverageEAExecutionTime());
        durationDataObject.addProperty("DurationEAExecutionMin",returnMinEAExecutionTime());
        durationDataObject.addProperty("FrameworkOverhead",frameworkOverhead);
        durationDataObject.addProperty("initialization Overhead", initializationOverhead);
        durationDataObject.addProperty("Containers Creation",durationIslandsCreation + durationSlavesCreation);
        durationDataObject.addProperty("MigrationOverheadMax", returnMigrationOverheadMax());
        durationDataObject.addProperty("MigrationOverheadMin", returnMigrationOverheadMin());
        durationDataObject.addProperty("MigrationOverheadAverage", returnMigrationOverheadAverage());
        durationDataObject.addProperty("Result Collection Overhead", frameworkOverhead - initializationOverhead - returnMigrationOverheadMax());
        durationDataObject.addProperty("numberOfMigrations",numberOfMigrationsFitness);
        dataToVisualizeObject.addProperty("Cost", constraintResultsValues[2]);
        dataToVisualizeObject.addProperty("DailyDeviation", constraintResultsValues[3]);
        dataToVisualizeObject.addProperty("NumberOfHourlyDeviation", constraintResultsValues[4]);
        dataToVisualizeObject.add("Duration data", durationDataObject);
        dataToVisualizeObject.add("data", dataToVisualizeArray);

        String jsonInString1 = gson.toJson(dataToVisualizeObject);
        String replacedJsonInString1 = jsonInString1.replaceAll("ResourcePlan", "resourcePlan");
        resultsCollection.add(replacedJsonInString1);

        if (experiment && jobConfigList.size() != 0) {
            //jRedisconn.flushAll();
            executionTimeEAs =  new HashMap<>();
            migrationsOverheads = new HashMap<>();
            jobConfig.readFromExistingJobConfig(jobConfigList.remove(0));
            logger.info("received job config: " + jobConfig.toString());
            algorithmManager.initialize(false);
        }
        else{
            executionTimeEAs =  new HashMap<>();
            migrationsOverheads = new HashMap<>();
            logger.info("All jobs are finished");
            jobId = 0;
            jobConfig.setNumberOfIslands(0);
        }


    }

    public String formatBestChromosome(String bestChromosome){
        String formattedChromosome = "1 2 1\n";
        String[] lines = bestChromosome.split("\\r?\\n");
        String[] firstLine = lines[0].split("\\s+");
        String numberOfGenes = firstLine[3];
        formattedChromosome = formattedChromosome.concat("0 0 " + numberOfGenes + "\n");
        for(int i = 0; i<lines.length; i++){
            String currentLine = lines[i];
            String[] currentLineArray = currentLine.split("\\s+");
            if(currentLineArray[0].equals("")) {
                String newLine = "";
                newLine = newLine.concat(currentLineArray[1] + "  ");
                newLine = newLine.concat(currentLineArray[2] + " ");
                newLine = newLine.concat(currentLineArray[3] + " ");
                newLine = newLine.concat(String.valueOf(Double.parseDouble(currentLineArray[4])));
                newLine = newLine.concat("\n");
                formattedChromosome = formattedChromosome.concat(newLine);
            }
        }
        return formattedChromosome;
    }

    private void sendFormattedChromosome(String formattedChromosome){
        ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.splittingJoiningURL+ "/sjs/population/1/slaves", formattedChromosome,String.class);

    }

    @RequestMapping(value = "/plan/{id}", method = RequestMethod.GET)
    public String getReq(@PathVariable int id) throws InterruptedException {
        return resultsCollection.get(id);
    }

    @RequestMapping(value = "/plan/all", method = RequestMethod.GET)
    public List<String> getReq() throws InterruptedException {
        return resultsCollection;
    }

}
