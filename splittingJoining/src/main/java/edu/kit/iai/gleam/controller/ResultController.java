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
import java.util.List;

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

    private List<String> aggregatedResult = new ArrayList<>();
    Gson gson = new Gson();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static int actualNumberOfGenerationOfOneJob = 0;


    /**
     * Called by all islands to send partial results
     * @param islandNumber
     */
    @RequestMapping(value = "/sjs/population/result", method = RequestMethod.POST)
    public void receiveResult(@RequestBody int islandNumber) {
        logger.trace("received result from island " + islandNumber);

        synchronized (aggregatedResult) {
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
     * Check whether all islands have sent their partial results
     * @return
     */
    private boolean isResultComplete() {
        RedisAtomicInteger amountOfIslands = new RedisAtomicInteger(ConstantStrings.numberOfIslands, intTemplate.getConnectionFactory());
        RedisAtomicInteger receivedResultsCounter = new RedisAtomicInteger(ConstantStrings.receivedResultsCounter, intTemplate.getConnectionFactory());
        int numberOfIslands = amountOfIslands.get();
        int receivedResults = receivedResultsCounter.get();
        logger.debug("number of islands: " + numberOfIslands + ", received results: " + receivedResults);
        if (receivedResults == numberOfIslands) {
            return true;
        }
        return false;

    }

    private void sendResultToCoordination() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String aggregatedResultJson = this.gson.toJson(this.aggregatedResult);
        HttpEntity<String> entity = new HttpEntity<String>(aggregatedResultJson, headers);
        logger.info("sending result");
        ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.coordinationURL + "/ojm/result", entity, String.class);
    }

    public void reset() {
        logger.debug("reset result list");
        aggregatedResult = new ArrayList<>();
    }
}
