package iai.kit.edu.controller;

import iai.kit.edu.config.ConstantStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

/**
 * Sends readiness status to Splitting & Joining Service
 */
@CrossOrigin(origins = "*")
public class SlavesReadinessController {
    private RestTemplate restTemplate = new RestTemplate();

    private final Logger logger=LoggerFactory.getLogger(this.getClass());


    public void sendReadinessStatus(String message) {
        logger.debug("sending initialized status");
        ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.coordinationURL +"/ojm/slaves/ready/", message, String.class);
    }
}
