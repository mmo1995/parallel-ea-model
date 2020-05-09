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
 * Creates and starts calculations
 */

@CrossOrigin(origins = "*")
@Component
public class SlaveController {

    private RestTemplate restTemplate = new RestTemplate();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Overhead overhead;


    public void createSlaves(int numberOfSlaves){
        logger.info("creating slaves");
        overhead.setStartSlaveCreation(System.currentTimeMillis());
        ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.containerManagementURL +"/com/create/slaves", numberOfSlaves, String.class);
    }
}
