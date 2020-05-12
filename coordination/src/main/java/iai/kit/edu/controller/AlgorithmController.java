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
import org.springframework.data.redis.connection.jedis.JedisConnection;
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
    private DynamicJobConfig dynamicJobConfig;

    private static RestTemplate restTemplate = new RestTemplate();

    private List<String> resultsCollection;

    private List<JobConfig> jobConfigList;

    private boolean experiment;

    private RedisAtomicInteger amountOfGeneration;

    private JedisConnectionFactory jedisConnectionFactory;
    private RedisConnection jRedisconn;
    private int numberOfMigrationsFitness = 0;
    private Map<Integer, Double> executionTimeIslands = new HashMap<>();
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
        jobConfig.readFromJson(json);
        logger.info("received job config: " + jobConfig.toString());
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
        Gson gson = new Gson();
        resultsCollection = new ArrayList<>();
        ExperimentConfig experimentConfig = gson.fromJson(json, ExperimentConfig.class);
        int[] numberOfIslands = experimentConfig.getNumberOfIslands();
        int[] numberOfSlaves = experimentConfig.getNumberOfSlaves();
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
        int[] numberOfGeneration = experimentConfig.getNumberOfGeneration();

        jobConfigList = new ArrayList<>();
        for (int i = 0; i < numberOfIslands.length; i++) {
            for (int ii = 0; ii < numberOfSlaves.length; ii++) {
                for (int j = 0; j < populationSizes.length; j++) {
                    for (int jj = 0; jj < numberOfGeneration.length; jj++) {
                        for (int k = 0; k < delays.length; k++) {
                            for (int l = 0; l < migrationRates.length; l++) {
                                for (int m = 0; m < topologies.length; m++) {
                                    JobConfig jobConfigTemp = new JobConfig();
                                    jobConfigTemp.setNumberOfIslands(numberOfIslands[i]);
                                    jobConfigTemp.setNumberOfSlaves(numberOfSlaves[ii]);
                                    jobConfigTemp.setGlobalPopulationSize(populationSizes[j]);
                                    jobConfigTemp.setNumberOfGeneration(numberOfGeneration[jj]);
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
                                    jobConfigTemp.setGlobalTerminationGeneration(globalTerminationGeneration_1);
                                    jobConfigTemp.setGlobalTerminationFitness(globalTerminationFitness_1);
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
        /*jedisConnectionFactory = redisConnection.returnJedisConnectionFactory();
        jRedisconn= jedisConnectionFactory.getConnection();
        jRedisconn.flushAll();*/
        jobConfig.readFromExistingJobConfig(jobConfigList.remove(0));
        logger.info("received job config: " + jobConfig.toString());
        amountOfGeneration.set(jobConfig.getEpochTerminationGeneration()+1);
        algorithmManager.initialize(false);
    }

    /**
     * Receive configuration for several optimization task
     * @param json of configurations
     */
    @RequestMapping(value = "/start/job/dynamic", method = RequestMethod.POST)
    public void receiveDynamicStartConfiguration(@RequestBody String json) {
        experiment = false;
        amountOfGeneration = new RedisAtomicInteger(ConstantStrings.gleamConfigurationsGeneration, template.getConnectionFactory());

/*        dynamicJobConfig.setNumberOfIslands(dynamicJobConfig.getNumberOfIslands());
        dynamicJobConfig.setNumberOfSlaves(dynamicJobConfig.getNumberOfSlaves());
        dynamicJobConfig.setAmountFitness(dynamicJobConfig.getAmountFitness());
        dynamicJobConfig.setGlobalPopulationSize(dynamicJobConfig.getGlobalPopulationSize());
        dynamicJobConfig.setDelay(dynamicJobConfig.getDelay());
        dynamicJobConfig.setMigrationRate(dynamicJobConfig.getMigrationRate());
        dynamicJobConfig.setTopology(dynamicJobConfig.getTopology());
        dynamicJobConfig.setEpochTerminationGeneration(dynamicJobConfig.getEpochTerminationGeneration());
        dynamicJobConfig.setEpochTerminationCriterion(dynamicJobConfig.getEpochTerminationCriterion());
        dynamicJobConfig.setEpochTerminationEvaluation(dynamicJobConfig.getEpochTerminationEvaluation());
        dynamicJobConfig.setEpochTerminationFitness(dynamicJobConfig.getEpochTerminationFitness());
        dynamicJobConfig.setEpochTerminationTime(dynamicJobConfig.getEpochTerminationTime());
        dynamicJobConfig.setEpochTerminationGDV(dynamicJobConfig.getEpochTerminationGDV());
        dynamicJobConfig.setEpochTerminationGAK(dynamicJobConfig.getEpochTerminationGAK());

        dynamicJobConfig.setGlobalTerminationCriterion(dynamicJobConfig.getGlobalTerminationCriterion());
        dynamicJobConfig.setGlobalTerminationEpoch(dynamicJobConfig.getGlobalTerminationEpoch());
        dynamicJobConfig.setGlobalTerminationEvaluation(dynamicJobConfig.getGlobalTerminationEvaluation());
        dynamicJobConfig.setGlobalTerminationFitness(dynamicJobConfig.getGlobalTerminationFitness());
        dynamicJobConfig.setGlobalTerminationGeneration(dynamicJobConfig.getGlobalTerminationGeneration());
        dynamicJobConfig.setNumberOfGeneration(dynamicJobConfig.getNumberOfGeneration());*/


        dynamicJobConfig.readFromJson(json);
        //amountOfGeneration.set(dynamicJobConfig.getEpochTerminationGeneration()+1);
        logger.info("received dynamic job config: " + dynamicJobConfig.toString());
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
    public synchronized void  receiveExecutionTime(@PathVariable String islandNumber, @PathVariable String sumEAExecution, @RequestBody String numberOfMigrations) {
        executionTimeIslands.put(Integer.parseInt(islandNumber), Double.parseDouble(sumEAExecution));
        numberOfMigrationsFitness = Integer.parseInt(numberOfMigrations);
        logger.info(islandNumber + "/ "+ numberOfMigrations + " /" + numberOfMigrationsFitness + "//" + returnMaxExecutionTime());

    }
    public double returnMaxExecutionTime ()
    {

        return Collections.max(executionTimeIslands.values());
    }
    public double returnMinExecutionTime ()
    {

        return Collections.min(executionTimeIslands.values());

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
        double duration= TimeUnit.MILLISECONDS.toSeconds(overhead.getEndEvolution() - overhead.getStartEvolution());
        double durationContainerCreation= TimeUnit.MILLISECONDS.toSeconds(overhead.getEndIslandCreation() - overhead.getStartIslandCreation());

        double frameworkOverhead = duration - returnMaxExecutionTime();


        String  constraintResults = constraintsAndresultJson.substring(0, constraintsAndresultJson.indexOf("#"));
        String resultJson = constraintsAndresultJson.substring(constraintsAndresultJson.indexOf("#")+1, constraintsAndresultJson.length());
        String [] splitconstraintResults = constraintResults.split("\n");
        String [] constraintResultsValues = splitconstraintResults[0].split(" ");

        jobId ++;
        logger.info("received the results of the optimal scheduling plan #"+ splitconstraintResults[0] + "#");
        logger.info("#####");

        JsonObject dataToVisualizeObject = new JsonObject();
        JsonArray dataToVisualizeArray = new JsonArray();
        Gson gson = new Gson();
        //String jsonInString = gson.toJson(resultJson);
        JsonObject finalSchPlan = new JsonParser().parse(resultJson).getAsJsonObject();

        dataToVisualizeArray.add(finalSchPlan);
        dataToVisualizeObject.addProperty("JobID", jobId);
        dataToVisualizeObject.addProperty("NumberOfIslands",jobConfig.getNumberOfIslands());
        dataToVisualizeObject.addProperty("NumberOfSlaves",jobConfig.getNumberOfSlaves());
        dataToVisualizeObject.addProperty("PopulationSize",jobConfig.getGlobalPopulationSize());
        dataToVisualizeObject.addProperty("Generation",jobConfig.getEpochTerminationGeneration());
        dataToVisualizeObject.addProperty("Container Creation",durationContainerCreation - 100);
        dataToVisualizeObject.addProperty("Duration",duration );
        dataToVisualizeObject.addProperty("DurationEAExecutionMax",returnMaxExecutionTime());
        dataToVisualizeObject.addProperty("DurationEAExecutionMin",returnMinExecutionTime());
        dataToVisualizeObject.addProperty("FrameworkOverhead",frameworkOverhead);
        dataToVisualizeObject.addProperty("numberOfMigrations",numberOfMigrationsFitness);
        dataToVisualizeObject.addProperty("Cost", constraintResultsValues[2]);
        dataToVisualizeObject.addProperty("DailyDeviation", constraintResultsValues[3]);
        dataToVisualizeObject.addProperty("NumberOfHourlyDeviation", constraintResultsValues[4]);
        dataToVisualizeObject.add("data", dataToVisualizeArray);

        String jsonInString1 = gson.toJson(dataToVisualizeObject);
        String replacedJsonInString1 = jsonInString1.replaceAll("ResourcePlan", "resourcePlan");
        resultsCollection.add(replacedJsonInString1);

        if (experiment && jobConfigList.size() != 0) {
            //jRedisconn.flushAll();
            executionTimeIslands =  new HashMap<>();
            jobConfig.readFromExistingJobConfig(jobConfigList.remove(0));
            logger.info("received job config: " + jobConfig.toString());
            algorithmManager.initialize(false);
        }
        else{
            executionTimeIslands =  new HashMap<>();
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
                newLine = newLine.concat(String.valueOf(Double.parseDouble(currentLineArray[14])));
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
