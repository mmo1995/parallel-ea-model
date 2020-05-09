package iai.kit.edu.controller;

import com.google.gson.Gson;
import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.IslandConfig;
import iai.kit.edu.core.Population;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import java.io.File;

/**
 * Sends result to Splitting & Joining Service
 */
@CrossOrigin(origins = "*")
public class ResultController {
    @Autowired
    IslandConfig islandConfig;

    @Autowired
    @Qualifier("stringTemplate")
    RedisTemplate<String, String> stringTemplate;

    @Autowired
    Population population;

    private RestTemplate restTemplate = new RestTemplate();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void sendResult() {

        String islandNr = String.valueOf(islandConfig.getIslandNumber());
        String URL = ConstantStrings.starterURL + "/opt/executiontime/";
        // String URL = ConstantStrings.starterURL+ islandNr + ":8090/opt/executiontime/";
        logger.info("URL "+ URL);
        ResponseEntity<String> answer2 = restTemplate.postForEntity(URL, islandConfig.getIslandNumber(), String.class);
        logger.info("sending result");
        Gson gson = new Gson();
        String resultJson = gson.toJson(population.getResult());
        ValueOperations<String, String> ops = this.stringTemplate.opsForValue();
        ops.set(ConstantStrings.resultPopulation + "." + islandConfig.getIslandNumber(), resultJson);
        ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.splittingJoiningURL + "/sjs/population/result/", islandConfig.getIslandNumber(), String.class);
    }
}
