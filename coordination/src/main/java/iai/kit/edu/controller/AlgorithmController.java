package iai.kit.edu.controller;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import iai.kit.edu.config.*;
import iai.kit.edu.core.AlgorithmManager;
import iai.kit.edu.core.Overhead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
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
    private List<HeteroJobConfig> heteroJobConfigList;
    private List<Boolean> heteroList;

    private boolean experiment;

    private boolean hetero;

    private boolean frontend;

    private RedisAtomicInteger amountOfGeneration;

    private JedisConnectionFactory jedisConnectionFactory;
    private RedisConnection jRedisconn;
    private int numberOfMigrationsFitness = 0;
    private Map<Integer, Double> executionTimeEAs = new HashMap<>();
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
        hetero = false;
        frontend = false;
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
        frontend = false;
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
        frontend = false;
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
        amountOfGeneration = new RedisAtomicInteger(ConstantStrings.gleamConfigurationsGeneration, template.getConnectionFactory());
        experiment = true;
        hetero = true;
        frontend = false;
        Gson gson = new Gson();
        resultsCollection = new ArrayList<>();
        HeteroExperimentConfig heteroExperimentConfig = gson.fromJson(json, HeteroExperimentConfig.class);
        int[] numberOfIslands = heteroExperimentConfig.getNumberOfIslands();
        int[] numberOfSlaves = heteroExperimentConfig.getNumberOfSlaves();
        int[] populationSizes = heteroExperimentConfig.getGlobalPopulationSize();
        int[] delays = heteroExperimentConfig.getDelay();
        int[][] numberOfGeneration = heteroExperimentConfig.getNumberOfGeneration();
        int[][] migrationRate = heteroExperimentConfig.getMigrationRate();
        String[] topology = heteroExperimentConfig.getTopology();
        String[][] initialSelectionPolicy = heteroExperimentConfig.getInitialSelectionPolicy();
        int[][] amountFitness = heteroExperimentConfig.getAmountFitness();
        String[] initialSelectionPolicyInitializer = heteroExperimentConfig.getInitialSelectionPolicyInitializer();
        int[] amountFitnessInitializer = heteroExperimentConfig.getAmountFitnessInitializer();
        String[][] selectionPolicy = heteroExperimentConfig.getSelectionPolicy();
        String[][] replacementPolicy = heteroExperimentConfig.getReplacementPolicy();
        int[][] demeSize = heteroExperimentConfig.getDemeSize();
        boolean[] asyncMigration = heteroExperimentConfig.getAsyncMigration();
        String[][] acceptRuleForOffspring = heteroExperimentConfig.getAcceptRuleForOffspring();
        double[][] rankingParameter = heteroExperimentConfig.getRankingParameter();
        double[][] minimalHammingDistance = heteroExperimentConfig.getMinimalHammingDistance();

        String[][] epochTerminationCriterion = heteroExperimentConfig.getEpochTerminationCriterion();
        int[][] epochTerminationEvaluation = heteroExperimentConfig.getEpochTerminationEvaluation();
        double[][] epochTerminationFitness = heteroExperimentConfig.getEpochTerminationFitness();
        int[][] epochTerminationGeneration = heteroExperimentConfig.getEpochTerminationGeneration();
        int[][] epochTerminationTime = heteroExperimentConfig.getEpochTerminationTime();
        int[][] epochTerminationGDV = heteroExperimentConfig.getEpochTerminationGDV();
        int[][] epochTerminationGAK = heteroExperimentConfig.getEpochTerminationGAK();

        String globalTerminationCriterion = heteroExperimentConfig.getGlobalTerminationCriterion();
        int globalTerminationEpoch = heteroExperimentConfig.getGlobalTerminationEpoch();
        int globalTerminationEvaluation = heteroExperimentConfig.getGlobalTerminationEvaluation();
        double globalTerminationFitness = heteroExperimentConfig.getGlobalTerminationFitness();
        int globalTerminationGeneration = heteroExperimentConfig.getGlobalTerminationGeneration();
        int globalTerminationTime = heteroExperimentConfig.getGlobalTerminationTime();
        int globalTerminationGDV = heteroExperimentConfig.getGlobalTerminationGDV();
        int globalTerminationGAK = heteroExperimentConfig.getGlobalTerminationGAK();

        heteroJobConfigList = new ArrayList<>();
        for(int i = 0; i< numberOfIslands.length; i++){
            for(int j = 0; j< numberOfSlaves.length; j++){
                for(int k = 0; k< populationSizes.length; k++){
                    for(int l = 0; l< delays.length; l++){
                        for(int m = 0; m< numberOfGeneration.length; m++){
                            for(int n = 0; n< migrationRate.length; n++){
                                for (int o = 0; o<topology.length; o++){
                                    for(int p = 0; p< initialSelectionPolicy.length; p++){
                                        for(int q = 0; q< amountFitness.length; q++){
                                            for(int r = 0; r< initialSelectionPolicyInitializer.length; r++){
                                                for(int s = 0; s< amountFitnessInitializer.length; s++){
                                                    for(int t = 0; t< selectionPolicy.length; t++){
                                                        for (int u = 0; u< replacementPolicy.length; u++){
                                                            for(int v = 0; v<demeSize.length; v++){
                                                                for(int w = 0; w< asyncMigration.length; w++){
                                                                    for(int x = 0; x< acceptRuleForOffspring.length; x++){
                                                                        for(int y = 0; y< rankingParameter.length; y++){
                                                                            for(int z = 0; z< minimalHammingDistance.length; z++){
                                                                                for(int ii = 0; ii<epochTerminationCriterion.length; ii++){
                                                                                    for(int jj = 0; jj< epochTerminationEvaluation.length; jj++){
                                                                                        for(int kk=0; kk< epochTerminationFitness.length; kk++){
                                                                                            for(int ll=0; ll< epochTerminationGeneration.length; ll++){
                                                                                                for(int mm=0; mm< epochTerminationTime.length; mm++ ){
                                                                                                    for(int nn=0; nn<epochTerminationGDV.length; nn++){
                                                                                                        for(int oo=0; oo<epochTerminationGAK.length;oo++){
                                                                                                            HeteroJobConfig jobConfigTemp = new HeteroJobConfig();
                                                                                                            jobConfigTemp.setNumberOfIslands(numberOfIslands[i]);
                                                                                                            jobConfigTemp.setNumberOfSlaves(numberOfSlaves[j]);
                                                                                                            jobConfigTemp.setGlobalPopulationSize(populationSizes[k]);
                                                                                                            jobConfigTemp.setDelay(delays[l]);
                                                                                                            jobConfigTemp.setNumberOfGeneration(numberOfGeneration[m]);
                                                                                                            jobConfigTemp.setMigrationRate(migrationRate[n]);
                                                                                                            jobConfigTemp.setTopology(topology[o]);
                                                                                                            jobConfigTemp.setInitialSelectionPolicy(initialSelectionPolicy[p]);
                                                                                                            jobConfigTemp.setAmountFitness(amountFitness[q]);
                                                                                                            jobConfigTemp.setInitialSelectionPolicyInitializer(initialSelectionPolicyInitializer[r]);
                                                                                                            jobConfigTemp.setAmountFitnessInitializer(amountFitnessInitializer[s]);
                                                                                                            jobConfigTemp.setSelectionPolicy(selectionPolicy[t]);
                                                                                                            jobConfigTemp.setReplacementPolicy(replacementPolicy[u]);
                                                                                                            jobConfigTemp.setDemeSize(demeSize[v]);
                                                                                                            jobConfigTemp.setAsyncMigration(asyncMigration[w]);
                                                                                                            jobConfigTemp.setAcceptRuleForOffspring(acceptRuleForOffspring[x]);
                                                                                                            jobConfigTemp.setRankingParameter(rankingParameter[y]);
                                                                                                            jobConfigTemp.setMinimalHammingDistance(minimalHammingDistance[z]);
                                                                                                            jobConfigTemp.setEpochTerminationCriterion(epochTerminationCriterion[ii]);
                                                                                                            jobConfigTemp.setEpochTerminationEvaluation(epochTerminationEvaluation[jj]);
                                                                                                            jobConfigTemp.setEpochTerminationFitness(epochTerminationFitness[kk]);
                                                                                                            jobConfigTemp.setEpochTerminationGeneration(epochTerminationGeneration[ll]);
                                                                                                            jobConfigTemp.setEpochTerminationTime(epochTerminationTime[mm]);
                                                                                                            jobConfigTemp.setEpochTerminationGDV(epochTerminationGDV[nn]);
                                                                                                            jobConfigTemp.setEpochTerminationGAK(epochTerminationGAK[oo]);
                                                                                                            jobConfigTemp.setGlobalTerminationCriterion(globalTerminationCriterion);
                                                                                                            jobConfigTemp.setGlobalTerminationEpoch(globalTerminationEpoch);
                                                                                                            jobConfigTemp.setGlobalTerminationEvaluation(globalTerminationEvaluation);
                                                                                                            jobConfigTemp.setGlobalTerminationFitness(globalTerminationFitness);
                                                                                                            jobConfigTemp.setGlobalTerminationGeneration(globalTerminationGeneration);
                                                                                                            jobConfigTemp.setGlobalTerminationTime(globalTerminationTime);
                                                                                                            jobConfigTemp.setGlobalTerminationGDV(globalTerminationGDV);
                                                                                                            jobConfigTemp.setGlobalTerminationGAK(globalTerminationGAK);
                                                                                                            heteroJobConfigList.add(jobConfigTemp);
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
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        heteroJobConfig.readFromExistingJobConfig(heteroJobConfigList.remove(0));
        logger.info("received job config: " + heteroJobConfig.toString());
        //amountOfGeneration.set(heteroJobConfig.getEpochTerminationGeneration()+1);
        overhead.setStartEvolution(System.currentTimeMillis());
        overhead.setStartInitializationOverhead(System.currentTimeMillis());
        algorithmManager.initialize(true);
    }

    /**
     * Receive configuration for multiple jobs from frontend
     * @param json of configurations
     */
    @RequestMapping(value = "/start/jobs/frontend", method = RequestMethod.POST)
    public void receiveFrontendStartConfiguration(@RequestBody String json) {
        amountOfGeneration = new RedisAtomicInteger(ConstantStrings.gleamConfigurationsGeneration, template.getConnectionFactory());
        experiment = true;
        frontend = true;
        resultsCollection = new ArrayList<>();
        jobConfigList = new ArrayList<>();
        heteroJobConfigList = new ArrayList<>();
        heteroList = new ArrayList<>();
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        JsonArray heteroConfiguration = jsonObject.get("heteroConfiguration").getAsJsonArray();
        jsonObject.remove("heteroConfiguration");


        for(int i = 0; i< heteroConfiguration.size(); i++){
            if(heteroConfiguration.get(i).getAsString().equals("true")){
                heteroList.add(true);
            } else{
                heteroList.add(false);
            }
        }
        JsonArray globalPopulationSizeArray = jsonObject.get("globalPopulationSize").getAsJsonArray();
        JsonArray numberOfIslandsArray = jsonObject.get("numberOfIslands").getAsJsonArray();
        JsonArray numberOfSlavesArray = jsonObject.get("numberOfSlaves").getAsJsonArray();
        JsonArray numberOfGenerationsArray = jsonObject.get("numberOfGenerations").getAsJsonArray();
        JsonArray migrationRateArray = jsonObject.get("migrationRate").getAsJsonArray();
        JsonArray topologyArray = jsonObject.get("topology").getAsJsonArray();
        JsonArray initialSelectionPolicyArray = jsonObject.get("initialSelectionPolicy").getAsJsonArray();
        JsonArray amountFitnessArray = jsonObject.get("amountFitness").getAsJsonArray();
        JsonArray initialSelectionPolicyInitializerArray = jsonObject.get("initialSelectionPolicyInitializer").getAsJsonArray();
        JsonArray amountFitnessInitializerArray = jsonObject.get("amountFitnessInitializer").getAsJsonArray();
        JsonArray selectionPolicyArray = jsonObject.get("selectionPolicy").getAsJsonArray();
        JsonArray replacementPolicyArray = jsonObject.get("replacementPolicy").getAsJsonArray();
        JsonArray demeSizeArray = jsonObject.get("demeSize").getAsJsonArray();
        JsonArray asyncMigrationArray = jsonObject.get("asyncMigration").getAsJsonArray();
        JsonArray acceptRuleForOffspringArray = jsonObject.get("acceptRuleForOffspring").getAsJsonArray();
        JsonArray rankingParameterArray = jsonObject.get("rankingParameter").getAsJsonArray();
        JsonArray minimalHammingDistanceArray = jsonObject.get("minimalHammingDistance").getAsJsonArray();
        JsonArray delayArray = jsonObject.get("delay").getAsJsonArray();
        JsonArray globalTerminationCriterionArray = jsonObject.get("globalTerminationCriterion").getAsJsonArray();
        JsonArray globalTerminationEpochArray = jsonObject.get("globalTerminationEpoch").getAsJsonArray();
        JsonArray globalTerminationEvaluationArray = jsonObject.get("globalTerminationEvaluation").getAsJsonArray();
        JsonArray globalTerminationFitnessArray = jsonObject.get("globalTerminationFitness").getAsJsonArray();
        JsonArray globalTerminationGenerationArray = jsonObject.get("globalTerminationGeneration").getAsJsonArray();
        JsonArray globalTerminationTimeArray = jsonObject.get("globalTerminationTime").getAsJsonArray();
        JsonArray globalTerminationGDVArray = jsonObject.get("globalTerminationGDV").getAsJsonArray();
        JsonArray globalTerminationGAKArray = jsonObject.get("globalTerminationGAK").getAsJsonArray();
        JsonArray epochTerminationCriterionArray = jsonObject.get("epochTerminationCriterion").getAsJsonArray();
        JsonArray epochTerminationEvaluationArray = jsonObject.get("epochTerminationEvaluation").getAsJsonArray();
        JsonArray epochTerminationFitnessArray = jsonObject.get("epochTerminationFitness").getAsJsonArray();
        JsonArray epochTerminationGenerationArray = jsonObject.get("epochTerminationGeneration").getAsJsonArray();
        JsonArray epochTerminationTimeArray = jsonObject.get("epochTerminationTime").getAsJsonArray();
        JsonArray epochTerminationGDVArray = jsonObject.get("epochTerminationGDV").getAsJsonArray();
        JsonArray epochTerminationGAKArray = jsonObject.get("epochTerminationGAK").getAsJsonArray();

        for(int i = 0; i< heteroList.size(); i++){
            if(heteroList.get(i) == true){
                HeteroJobConfig heteroJobConfig = new HeteroJobConfig();
                heteroJobConfig.setGlobalPopulationSize(globalPopulationSizeArray.get(i).getAsInt());
                heteroJobConfig.setNumberOfIslands(numberOfIslandsArray.get(i).getAsInt());
                heteroJobConfig.setNumberOfSlaves(numberOfSlavesArray.get(i).getAsInt());
                int[] numberOfGenerations = new int[numberOfGenerationsArray.get(i).getAsJsonArray().size()];
                for(int j = 0; j<numberOfGenerations.length; j++){
                    numberOfGenerations[j] = numberOfGenerationsArray.get(i).getAsJsonArray().get(j).getAsInt();
                }
                heteroJobConfig.setNumberOfGeneration(numberOfGenerations);
                int[] migrationRates = new int[migrationRateArray.get(i).getAsJsonArray().size()];
                for(int k = 0; k<migrationRates.length; k++){
                    migrationRates[k] = migrationRateArray.get(i).getAsJsonArray().get(k).getAsInt();
                }
                heteroJobConfig.setMigrationRate(migrationRates);
                heteroJobConfig.setTopology(topologyArray.get(i).getAsString());
                String[] initialSelectionPolicy = new String[initialSelectionPolicyArray.get(i).getAsJsonArray().size()];
                for(int l = 0; l<initialSelectionPolicy.length; l++){
                    initialSelectionPolicy[l] = initialSelectionPolicyArray.get(i).getAsJsonArray().get(l).getAsString();
                }
                heteroJobConfig.setInitialSelectionPolicy(initialSelectionPolicy);
                int[] amountFitness = new int[amountFitnessArray.get(i).getAsJsonArray().size()];
                for(int m = 0; m<amountFitness.length; m++){
                    amountFitness[m] = amountFitnessArray.get(i).getAsJsonArray().get(m).getAsInt();
                }
                heteroJobConfig.setAmountFitness(amountFitness);
                heteroJobConfig.setInitialSelectionPolicyInitializer(initialSelectionPolicyInitializerArray.get(i).getAsString());
                heteroJobConfig.setAmountFitnessInitializer(amountFitnessInitializerArray.get(i).getAsInt());
                String[] selectionPolicy = new String[selectionPolicyArray.get(i).getAsJsonArray().size()];
                for(int n = 0; n<selectionPolicy.length; n++){
                    selectionPolicy[n] = selectionPolicyArray.get(i).getAsJsonArray().get(n).getAsString();
                }
                heteroJobConfig.setSelectionPolicy(selectionPolicy);
                String[] replacementPolicy = new String[replacementPolicyArray.get(i).getAsJsonArray().size()];
                for(int n = 0; n<selectionPolicy.length; n++){
                    replacementPolicy[n] = replacementPolicyArray.get(i).getAsJsonArray().get(n).getAsString();
                }
                heteroJobConfig.setReplacementPolicy(replacementPolicy);
                int[] demeSize = new int[demeSizeArray.get(i).getAsJsonArray().size()];
                for(int o = 0; o<demeSize.length; o++){
                    demeSize[o] = demeSizeArray.get(i).getAsJsonArray().get(o).getAsInt();
                }
                heteroJobConfig.setDemeSize(demeSize);
                heteroJobConfig.setAsyncMigration(asyncMigrationArray.get(i).getAsBoolean());
                String[] acceptRuleForOffspring = new String[acceptRuleForOffspringArray.get(i).getAsJsonArray().size()];
                for(int p = 0; p< acceptRuleForOffspring.length; p++){
                    acceptRuleForOffspring[p] = acceptRuleForOffspringArray.get(i).getAsJsonArray().get(p).getAsString();
                }
                heteroJobConfig.setAcceptRuleForOffspring(acceptRuleForOffspring);
                double[] rankingParameter = new double[rankingParameterArray.get(i).getAsJsonArray().size()];
                for(int q = 0; q< rankingParameter.length; q++){
                    rankingParameter[q] = rankingParameterArray.get(i).getAsJsonArray().get(q).getAsDouble();
                }
                heteroJobConfig.setRankingParameter(rankingParameter);
                double[] minimalHammingDistance = new double[minimalHammingDistanceArray.get(i).getAsJsonArray().size()];
                for(int r = 0; r<minimalHammingDistance.length;r++){
                    minimalHammingDistance[r] = minimalHammingDistanceArray.get(i).getAsJsonArray().get(r).getAsDouble();
                }
                heteroJobConfig.setMinimalHammingDistance(minimalHammingDistance);
                heteroJobConfig.setDelay(delayArray.get(i).getAsInt());
                heteroJobConfig.setGlobalTerminationCriterion(globalTerminationCriterionArray.get(i).getAsString());
                heteroJobConfig.setGlobalTerminationGeneration(globalTerminationGenerationArray.get(i).getAsInt());
                heteroJobConfig.setGlobalTerminationEpoch(globalTerminationEpochArray.get(i).getAsInt());
                heteroJobConfig.setGlobalTerminationFitness(globalTerminationFitnessArray.get(i).getAsInt());
                heteroJobConfig.setGlobalTerminationEvaluation(globalTerminationEvaluationArray.get(i).getAsInt());
                heteroJobConfig.setGlobalTerminationTime(globalTerminationTimeArray.get(i).getAsInt());
                heteroJobConfig.setGlobalTerminationGDV(globalTerminationGDVArray.get(i).getAsInt());
                heteroJobConfig.setGlobalTerminationGAK(globalTerminationGAKArray.get(i).getAsInt());
                String[] epochTerminationCriterion = new String[epochTerminationCriterionArray.get(i).getAsJsonArray().size()];
                for(int s = 0; s<epochTerminationCriterion.length; s++){
                    epochTerminationCriterion[s] = epochTerminationCriterionArray.get(i).getAsJsonArray().get(s).getAsString();
                }
                heteroJobConfig.setEpochTerminationCriterion(epochTerminationCriterion);
                double[] epochTerminationFitness = new double[epochTerminationFitnessArray.get(i).getAsJsonArray().size()];
                for(int t= 0; t<epochTerminationFitness.length; t++){
                    epochTerminationFitness[t] = epochTerminationFitnessArray.get(i).getAsJsonArray().get(t).getAsDouble();
                }
                heteroJobConfig.setEpochTerminationFitness(epochTerminationFitness);
                int[] epochTerminationGeneration = new int[epochTerminationGenerationArray.get(i).getAsJsonArray().size()];
                for(int u= 0; u<epochTerminationGeneration.length; u++){
                    epochTerminationGeneration[u] = epochTerminationGenerationArray.get(i).getAsJsonArray().get(u).getAsInt();
                }
                heteroJobConfig.setEpochTerminationGeneration(epochTerminationGeneration);
                int[] epochTerminationEvaluation = new int[epochTerminationEvaluationArray.get(i).getAsJsonArray().size()];
                for(int v= 0; v<epochTerminationEvaluation.length; v++){
                    epochTerminationEvaluation[v] = epochTerminationEvaluationArray.get(i).getAsJsonArray().get(v).getAsInt();
                }
                heteroJobConfig.setEpochTerminationEvaluation(epochTerminationEvaluation);
                int[] epochTerminationTime = new int[epochTerminationTimeArray.get(i).getAsJsonArray().size()];
                for(int w= 0; w<epochTerminationTime.length; w++){
                    epochTerminationTime[w] = epochTerminationTimeArray.get(i).getAsJsonArray().get(w).getAsInt();
                }
                heteroJobConfig.setEpochTerminationTime(epochTerminationTime);
                int[] epochTerminationGAK = new int[epochTerminationGAKArray.get(i).getAsJsonArray().size()];
                for(int x= 0; x<epochTerminationGAK.length; x++){
                    epochTerminationGAK[x] = epochTerminationGAKArray.get(i).getAsJsonArray().get(x).getAsInt();
                }
                heteroJobConfig.setEpochTerminationGAK(epochTerminationGAK);
                int[] epochTerminationGDV = new int[epochTerminationGDVArray.get(i).getAsJsonArray().size()];
                for(int y= 0; y<epochTerminationGDV.length; y++){
                    epochTerminationGDV[y] = epochTerminationGDVArray.get(i).getAsJsonArray().get(y).getAsInt();
                }
                heteroJobConfig.setEpochTerminationGDV(epochTerminationGDV);
                heteroJobConfigList.add(heteroJobConfig);
            } else {
                JobConfig jobConfig = new JobConfig();
                jobConfig.setGlobalPopulationSize(globalPopulationSizeArray.get(i).getAsInt());
                jobConfig.setNumberOfIslands(numberOfIslandsArray.get(i).getAsInt());
                jobConfig.setNumberOfSlaves(numberOfSlavesArray.get(i).getAsInt());
                jobConfig.setNumberOfGeneration(numberOfGenerationsArray.get(i).getAsInt());
                jobConfig.setMigrationRate(migrationRateArray.get(i).getAsInt());
                jobConfig.setTopology(topologyArray.get(i).getAsString());
                jobConfig.setInitialSelectionPolicy(initialSelectionPolicyArray.get(i).getAsString());
                jobConfig.setAmountFitness(amountFitnessArray.get(i).getAsInt());
                jobConfig.setInitialSelectionPolicyInitializer(initialSelectionPolicyInitializerArray.get(i).getAsString());
                jobConfig.setAmountFitnessInitializer(amountFitnessInitializerArray.get(i).getAsInt());
                jobConfig.setSelectionPolicy(selectionPolicyArray.get(i).getAsString());
                jobConfig.setReplacementPolicy(replacementPolicyArray.get(i).getAsString());
                jobConfig.setDemeSize(demeSizeArray.get(i).getAsInt());
                jobConfig.setAsyncMigration(asyncMigrationArray.get(i).getAsBoolean());
                jobConfig.setAcceptRuleForOffspring(acceptRuleForOffspringArray.get(i).getAsString());
                jobConfig.setRankingParameter(rankingParameterArray.get(i).getAsDouble());
                jobConfig.setMinimalHammingDistance(minimalHammingDistanceArray.get(i).getAsDouble());
                jobConfig.setDelay(delayArray.get(i).getAsInt());
                jobConfig.setGlobalTerminationCriterion(globalTerminationCriterionArray.get(i).getAsString());
                jobConfig.setGlobalTerminationGeneration(globalTerminationGenerationArray.get(i).getAsInt());
                jobConfig.setGlobalTerminationEpoch(globalTerminationEpochArray.get(i).getAsInt());
                jobConfig.setGlobalTerminationFitness(globalTerminationFitnessArray.get(i).getAsInt());
                jobConfig.setGlobalTerminationEvaluation(globalTerminationEvaluationArray.get(i).getAsInt());
                jobConfig.setGlobalTerminationTime(globalTerminationTimeArray.get(i).getAsInt());
                jobConfig.setGlobalTerminationGDV(globalTerminationGDVArray.get(i).getAsInt());
                jobConfig.setGlobalTerminationGAK(globalTerminationGAKArray.get(i).getAsInt());
                jobConfig.setEpochTerminationCriterion(epochTerminationCriterionArray.get(i).getAsString());
                jobConfig.setEpochTerminationFitness(epochTerminationFitnessArray.get(i).getAsDouble());
                jobConfig.setEpochTerminationEvaluation(epochTerminationEvaluationArray.get(i).getAsInt());
                jobConfig.setEpochTerminationGeneration(epochTerminationGenerationArray.get(i).getAsInt());
                jobConfig.setEpochTerminationTime(epochTerminationTimeArray.get(i).getAsInt());
                jobConfig.setEpochTerminationGDV(epochTerminationGDVArray.get(i).getAsInt());
                jobConfig.setEpochTerminationGAK(epochTerminationGAKArray.get(i).getAsInt());
                jobConfigList.add(jobConfig);
            }
        }
        if(heteroList.get(0)){
            hetero = true;
            heteroJobConfig.readFromExistingJobConfig(heteroJobConfigList.remove(0));
            logger.info("received job config: " + heteroJobConfig.toString());
            //amountOfGeneration.set(heteroJobConfig.getEpochTerminationGeneration()+1);
            overhead.setStartEvolution(System.currentTimeMillis());
            overhead.setStartInitializationOverhead(System.currentTimeMillis());
            algorithmManager.initialize(true);
            heteroList.remove(0);
        } else {
            hetero = false;
            jobConfig.readFromExistingJobConfig(jobConfigList.remove(0));
            logger.info("received job config: " + jobConfig.toString());
            //amountOfGeneration.set(heteroJobConfig.getEpochTerminationGeneration()+1);
            overhead.setStartEvolution(System.currentTimeMillis());
            overhead.setStartInitializationOverhead(System.currentTimeMillis());
            algorithmManager.initialize(false);
            heteroList.remove(0);
        }
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
        String islandExecutiontime = migrationOverhead.substring(0,migrationOverhead.indexOf("#"));
        String numberOfMigrations = migrationOverhead.substring(migrationOverhead.indexOf("#")+1);

        executionTimeEAs.put(Integer.parseInt(islandNumber), Double.parseDouble(sumEAExecution));
        executionTimeIslands.put(Integer.parseInt(islandNumber), Double.parseDouble(islandExecutiontime));
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
        return Collections.max(executionTimeIslands.values());
    }
    public double returnMigrationOverheadMin()
    {
        return Collections.min(executionTimeIslands.values());
    }
    public double returnMigrationOverheadAverage()
    {
        double sum = 0.0;
        for(double d: executionTimeIslands.values()){
            sum += d;
        }
        return sum / (double) executionTimeIslands.size();
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
        durationDataObject.addProperty("IslandExecutionTime Max", returnMigrationOverheadMax());
        durationDataObject.addProperty("IslandExecutionTime Min", returnMigrationOverheadMin());
        durationDataObject.addProperty("IslandExecutionTime Average", returnMigrationOverheadAverage());
        durationDataObject.addProperty("Result Collection Overhead", frameworkOverhead - initializationOverhead - (returnMigrationOverheadMax() - returnMaxEAExecutionTime()));
        durationDataObject.addProperty("numberOfMigrations",numberOfMigrationsFitness);
        dataToVisualizeObject.addProperty("Cost", constraintResultsValues[2]);
        dataToVisualizeObject.addProperty("DailyDeviation", constraintResultsValues[3]);
        dataToVisualizeObject.addProperty("NumberOfHourlyDeviation", constraintResultsValues[4]);
        dataToVisualizeObject.add("Duration data", durationDataObject);
        dataToVisualizeObject.add("data", dataToVisualizeArray);

        String jsonInString1 = gson.toJson(dataToVisualizeObject);
        String replacedJsonInString1 = jsonInString1.replaceAll("ResourcePlan", "resourcePlan");
        resultsCollection.add(replacedJsonInString1);
        if(frontend){
            if(heteroList.size()!= 0){
                if(heteroList.get(0)){
                    hetero = true;
                    heteroJobConfig.readFromExistingJobConfig(heteroJobConfigList.remove(0));
                    logger.info("received job config: " + heteroJobConfig.toString());
                    //amountOfGeneration.set(heteroJobConfig.getEpochTerminationGeneration()+1);
                    overhead.setStartEvolution(System.currentTimeMillis());
                    overhead.setStartInitializationOverhead(System.currentTimeMillis());
                    algorithmManager.initialize(true);
                    heteroList.remove(0);
                } else {
                    hetero = false;
                    jobConfig.readFromExistingJobConfig(jobConfigList.remove(0));
                    logger.info("received job config: " + jobConfig.toString());
                    //amountOfGeneration.set(heteroJobConfig.getEpochTerminationGeneration()+1);
                    overhead.setStartEvolution(System.currentTimeMillis());
                    overhead.setStartInitializationOverhead(System.currentTimeMillis());
                    algorithmManager.initialize(false);
                    heteroList.remove(0);
                }
            } else {
                executionTimeEAs =  new HashMap<>();
                executionTimeIslands = new HashMap<>();
                logger.info("All jobs are finished");
                jobId = 0;
                if(hetero){
                    heteroJobConfig.setNumberOfIslands(0);
                } else{
                    jobConfig.setNumberOfIslands(0);
                }
            }
        } else
        if (experiment && !hetero && jobConfigList.size() != 0) {
            //jRedisconn.flushAll();
            executionTimeEAs =  new HashMap<>();
            executionTimeIslands = new HashMap<>();
            jobConfig.readFromExistingJobConfig(jobConfigList.remove(0));
            logger.info("received job config: " + jobConfig.toString());
            overhead.setStartEvolution(System.currentTimeMillis());
            overhead.setStartInitializationOverhead(System.currentTimeMillis());
            algorithmManager.initialize(false);
        }
        else if (experiment && hetero && heteroJobConfigList.size() != 0){
            executionTimeEAs =  new HashMap<>();
            executionTimeIslands = new HashMap<>();
            heteroJobConfig.readFromExistingJobConfig(heteroJobConfigList.remove(0));
            logger.info("received job config: " + heteroJobConfig.toString());
            overhead.setStartEvolution(System.currentTimeMillis());
            overhead.setStartInitializationOverhead(System.currentTimeMillis());
            algorithmManager.initialize(true);
        }
        else {
            executionTimeEAs =  new HashMap<>();
            executionTimeIslands = new HashMap<>();
            logger.info("All jobs are finished");
            jobId = 0;
            if(hetero){
                heteroJobConfig.setNumberOfIslands(0);
            } else{
                jobConfig.setNumberOfIslands(0);
            }
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
