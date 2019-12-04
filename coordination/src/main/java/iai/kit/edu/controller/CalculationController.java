package iai.kit.edu.controller;

import iai.kit.edu.config.ConstantStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

/**
 * Creates and starts calculations
 */

@CrossOrigin(origins = "*")
@Component
public class CalculationController {

    private RestTemplate restTemplate = new RestTemplate();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public void createCalculations(int numberOfCalculations){
        logger.info("creating calculations");
        ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.containerManagementURL +"/com/create/calculations", numberOfCalculations, String.class);
    }
}
