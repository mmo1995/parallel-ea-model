package iai.kit.edu.controller;

import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.core.Overhead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

/**
 * Creates and starts islands
 */

@CrossOrigin(origins = "*")
@Component
public class IslandController {

    private RestTemplate restTemplate = new RestTemplate();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Overhead overhead;

    public void createIslands(int numberOfIslands){
        overhead.setStartIslandCreation(System.currentTimeMillis());
        logger.info("creating islands");
        ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.containerManagementURL +"/com/create/islands", numberOfIslands, String.class);
    }
}
