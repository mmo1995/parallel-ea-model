package edu.kit.iai.gleam.controller;

import com.google.gson.Gson;
import edu.kit.iai.gleam.config.ConstantStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Receives the partial results from all islands, joins them and sends them to the Coordination Service
 */
@CrossOrigin(origins = "*")
@RestController
public class ResultController {

    @Autowired
    @Qualifier("integerTemplate")
    RedisTemplate<String, Integer> intTemplate;

    @Autowired
    @Qualifier("stringTemplate")
    RedisTemplate<String, String> stringTemplate;

    @Autowired
    RestTemplate restTemplate;

    private String header = "";

    private String finalplan;

    private List<String> aggregatedResult = new ArrayList<>();

    //private StringBuilder aggregatedSlavesResult = new StringBuilder("");

    private Map<String, String> aggregatedSlavesResult = new HashMap<>();

    Gson gson = new Gson();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RedisAtomicInteger amountOfGeneration;

    private int numberOfGenerationOfOneJob;

    public static int actualNumberOfGenerationOfOneJob = 0;

    @Autowired
    Island isl;


    /**
     * Called by all islands to send partial results
     * @param islandNumber
     */
    @RequestMapping(value = "/sjs/population/result", method = RequestMethod.POST)
    public void receiveResult(@RequestBody int islandNumber) {

        synchronized (aggregatedResult) {
            logger.info("Received Result from island " + islandNumber);
            RedisAtomicInteger receivedResultsCounter = new RedisAtomicInteger(ConstantStrings.receivedResultsCounter, intTemplate.getConnectionFactory());
            receivedResultsCounter.incrementAndGet();

            ValueOperations<String, String> ops = this.stringTemplate.opsForValue();
            String resultJson = ops.get(ConstantStrings.resultPopulation + "." + islandNumber);

            List<String> result = this.gson.fromJson(resultJson, List.class);

            aggregatedResult.addAll(result);
            if (isResultComplete()) {
                sendResultToCoordination();
            }

        }

    }

    /**
     * Called by all slaves to send partial results
     * @param islandAndSlaveNumber
     */
    @RequestMapping(value = "/sjs/slavesPopulation/result", method = RequestMethod.POST)
    public void  receiveResultFromSlave(@RequestBody int[] islandAndSlaveNumber) {


        synchronized (aggregatedSlavesResult) {
            int islandNumber = islandAndSlaveNumber[0];
            int slaveNumber = islandAndSlaveNumber[1];
            //
            //logger.info("received result from island " + islandNumber + " received result from slave " + slaveNumber);
            RedisAtomicInteger receivedSlavesResultsCounter = new RedisAtomicInteger(ConstantStrings.receivedSlavesResultsCounter + "." + islandNumber, intTemplate.getConnectionFactory());
            receivedSlavesResultsCounter.incrementAndGet();
            ValueOperations<String, String> ops = this.stringTemplate.opsForValue();
            String resultJson = ops.get(ConstantStrings.resultPopulation + "." + islandNumber + "." + slaveNumber);
            String idNumber = resultJson.substring(resultJson.indexOf("#")+1);
            resultJson = resultJson.substring(0, resultJson.indexOf("#"));
            String[] numberOfChromosomes = resultJson.split("\n");
            //System.out.println("resultJson is" + resultJson);
            String result = aggregatedSlavesResult.get(String.valueOf(islandNumber));
            if(result == null){
                result = "";
            }
            result = result.concat(resultJson);
            aggregatedSlavesResult.put(String.valueOf(islandNumber),result);
            if(numberOfChromosomes.length>1){
                if(isIntermediateResultComplete(islandNumber, idNumber)){
                    sendResultToStarter(islandNumber);
                }
            } else{
                sendFinalResultToCoordination(resultJson, islandNumber);
            }

        }

    }

    @RequestMapping(value = "/opt/finalplan", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE)
    public void finalplan(@RequestBody String receivedFinalPlan) {
            //logger.info("received the optimal scheduling plan with real values");
            saveFinalPlan(receivedFinalPlan);
    }

    public synchronized void saveFinalPlan (String saveFinalPlane)
    {
        finalplan = saveFinalPlane;
    }


    /**
     * Check whether all islands have sent their partial results
     * @return
     */
    private boolean isResultComplete() {
        RedisAtomicInteger amountOfIslands = new RedisAtomicInteger(ConstantStrings.numberOfIslands, intTemplate.getConnectionFactory());
        RedisAtomicInteger receivedResultsCounter = new RedisAtomicInteger(ConstantStrings.receivedResultsCounter, intTemplate.getConnectionFactory());
        int numberOfIslands = amountOfIslands.get();
        int receivedResults = receivedResultsCounter.get();
        //logger.debug("number of islands: " + numberOfIslands + ", received results: " + receivedResults);
        if (receivedResults == numberOfIslands) {
            return true;
        }
        return false;

    }

    /**
     * Check whether all slaves have sent their partial resultsnumberOfIslands
     * @return
     */
    private boolean isIntermediateResultComplete(int islandNumber, String idNumber) {
        RedisAtomicInteger receivedSlavesResultsCounter = new RedisAtomicInteger(ConstantStrings.receivedSlavesResultsCounter + "." + islandNumber, intTemplate.getConnectionFactory());
        ValueOperations<String, Integer> ops = this.intTemplate.opsForValue();
        int numberOfSlaves = ops.get(ConstantStrings.numberOfSlavesTopic);
        int receivedSlavesResults = receivedSlavesResultsCounter.get();
        //logger.debug("number of slaves: " + numberOfSlaves + ", received results: " + receivedSlavesResults);
        if (receivedSlavesResults == numberOfSlaves) {
            receivedSlavesResultsCounter.set(0);
            String[] chromosomes = aggregatedSlavesResult.get(String.valueOf(islandNumber)).split("\n");
            int numberOfChromosomes = chromosomes.length;
            header = "0" +" "+numberOfChromosomes+" "+"4"+"\n";
            //System.out.println("header is " + header);
            header = header + aggregatedSlavesResult.get(String.valueOf(islandNumber));
            header = header.concat("#" + idNumber);
            return true;
        }
        return false;

    }

    private void sendResultToCoordination() {
        RedisAtomicInteger receivedResultsCounter = new RedisAtomicInteger(ConstantStrings.receivedResultsCounter, intTemplate.getConnectionFactory());
        receivedResultsCounter.set(0);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String aggregatedResultJson = this.gson.toJson(this.aggregatedResult);
        HttpEntity<String> entity = new HttpEntity<String>(aggregatedResultJson, headers);
        //logger.info("sending result");
        ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.coordinationURL + "/ojm/result", entity, String.class);
    }

    private void sendFinalResultToCoordination(String finalResultCol, int islandNumber){
        //logger.info("sending final result");
        RedisAtomicInteger receivedSlavesResultsCounter = new RedisAtomicInteger(ConstantStrings.receivedSlavesResultsCounter + "." + islandNumber, intTemplate.getConnectionFactory());
        receivedSlavesResultsCounter.set(0);
        RedisAtomicInteger numberOfGenerationForOneIsland = new RedisAtomicInteger(ConstantStrings.numberOfGenerationForOneIsland + "." + islandNumber, intTemplate.getConnectionFactory());
        numberOfGenerationForOneIsland.set(0);
        aggregatedSlavesResult.put(String.valueOf(islandNumber), "");
        System.out.print("Waiting for final plan ...");
        while(finalplan == null){
            //Wait for final plan to be received
            System.out.print(".");
        }
        ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.coordinationURL +"/ojm/finalResult", finalResultCol+"#"+finalplan, String.class);
        ResponseEntity<String> answer2 = restTemplate.getForEntity(ConstantStrings.starterURL + "/opt/" + islandNumber + "/resetTaskID", String.class);
        finalplan = null;
    }


    private void sendResultToStarter(int islandNumber) {
        RedisAtomicInteger numberOfGenerationForOneIsland = new RedisAtomicInteger(ConstantStrings.numberOfGenerationForOneIsland + "." + islandNumber, intTemplate.getConnectionFactory());
        int actualNumberOfGenerationOfOneJob = numberOfGenerationForOneIsland.incrementAndGet();

      /*  HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String aggregatedResultJson = this.gson.toJson(this.aggregatedResult);
        HttpEntity<String> entity = new HttpEntity<String>(aggregatedResultJson, headers);*/
        amountOfGeneration = new RedisAtomicInteger(ConstantStrings.gleamConfigurationsGeneration, intTemplate.getConnectionFactory());
        numberOfGenerationOfOneJob = amountOfGeneration.get();
        if (actualNumberOfGenerationOfOneJob != numberOfGenerationOfOneJob) {
            //logger.info("sending back the result of one generation");
            ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.starterURL + "/opt/" + islandNumber+ "/result", header, String.class);
            //logger.info("sending back the result of one generation to island  " + islandNumber);
            aggregatedSlavesResult.put(String.valueOf(islandNumber), "");
        }
        else
        {
            actualNumberOfGenerationOfOneJob = 0;
            //amountOfGeneration.set(0);
            //logger.info("sending back the result of last generation to island " + islandNumber);
            ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.starterURL + "/opt/" + islandNumber+ "/result", header, String.class);
            aggregatedSlavesResult.put(String.valueOf(islandNumber), "");
            //logger.info("one job is done");
            //logger.info("******************************************");
            /*ValueOperations<String, String> ops = this.stringTemplate.opsForValue();
            if (isl.numberOfChromosomes == 1)
            {
                ops.set(ConstantStrings.stopSubscribing + "." + 1, "stop");
                logger.info("stop signal is sent to slave Nr. 1");
                stopingPublisher.publish("stop", 1);
            }*/
        }
    }

    public void reset() {
        //logger.debug("reset result list");
        aggregatedResult = new ArrayList<>();
    }


}
