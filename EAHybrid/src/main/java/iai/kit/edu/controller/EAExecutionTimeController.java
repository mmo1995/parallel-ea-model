package iai.kit.edu.controller;

import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.core.ExecutionTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@CrossOrigin(origins = "*")
@RestController

public class EAExecutionTimeController {
    @Autowired
    ExecutionTime eaExecutionTime;
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${island.number}")
    private int islandNumber;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/opt/executiontime", method = RequestMethod.POST)
    public  void  receiveExecutionTime(@RequestBody String islandNumber) {
        double sumEAExecution = eaExecutionTime.returnSumExecutionTime();
        logger.info("sumEAExecution "+sumEAExecution);
        int numberOfMigrations = eaExecutionTime.returnNumberOfMigrations();
        String sumEAExecutionString = String.valueOf(sumEAExecution);
        String numberOfMigrationsString = String.valueOf(numberOfMigrations);
        String islandNumberString = String.valueOf(islandNumber);
        ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.coordinationURL+"/ojm/"+islandNumberString+"/"+sumEAExecutionString+"/executiontime", numberOfMigrationsString, String.class);
        logger.info("sending  numberOfMigrations "+ numberOfMigrations + "/islandNumberString " + islandNumberString);
        //TODO
        //StarterController starter = new StarterController();
        //starter.resttaskID();
        eaExecutionTime.resetExecutionTimeIslands();

    }

}






