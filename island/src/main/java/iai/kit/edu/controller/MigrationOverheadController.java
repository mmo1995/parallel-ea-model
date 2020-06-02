package iai.kit.edu.controller;

import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.IslandConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * Receives the execution time of each epoch from EA and sends it with the migration
 * overhead to the coordination service
 */
@CrossOrigin(origins = "*")
@RestController
public class MigrationOverheadController {

    private RestTemplate restTemplate = new RestTemplate();

    private static long startIslandExecution = 0;


    private static long endIslandExecution = 0;
    private static int eaExecutiontime = 0;

    private static int numberOfMigrations = 0;

    @Autowired
    IslandConfig islandConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static long getStartIslandExecution() {
        return startIslandExecution;
    }

    public static void setStartIslandExecution(long startIslandExecution) {
        MigrationOverheadController.startIslandExecution = startIslandExecution;
    }

    public static long getEndIslandExecution() {
        return endIslandExecution;
    }

    public static void setEndIslandExecution(long endIslandExecution) {
        MigrationOverheadController.endIslandExecution = endIslandExecution;
    }

    public static int getEaExecutiontime() {
        return eaExecutiontime;
    }

    public static void setEaExecutiontime(int eaExecutiontime) {
        MigrationOverheadController.eaExecutiontime = eaExecutiontime;
    }

    public static int getNumberOfMigrations() {
        return numberOfMigrations;
    }

    public static void setNumberOfMigrations(int numberOfMigrations) {
        MigrationOverheadController.numberOfMigrations = numberOfMigrations;
    }

    public void sendExecutiontimeToCoordination(){
        String executiontime = TimeUnit.MILLISECONDS.toSeconds(endIslandExecution - startIslandExecution) + "";
        logger.info("Execution time of Island: " + executiontime + " seconds");
        logger.info("Sum execution times of EA: " + eaExecutiontime + " seconds");
        logger.info("Migration overhead: " + (Integer.parseInt(executiontime) - eaExecutiontime) + " seconds");
        String islandNumberString = String.valueOf(islandConfig.getIslandNumber());
        String sumEAExecutionString = String.valueOf(eaExecutiontime);
        String MigrationoverheadString = String.valueOf((Integer.parseInt(executiontime) - eaExecutiontime));
        MigrationoverheadString = MigrationoverheadString.concat("#").concat(String.valueOf(numberOfMigrations));
        ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.coordinationURL+"/ojm/"+islandNumberString+"/"+sumEAExecutionString+"/executiontime", MigrationoverheadString, String.class);
        setStartIslandExecution(0);
        setEndIslandExecution(0);
        setNumberOfMigrations(0);
        setEaExecutiontime(0);
    }

    public static void addEAExecutiontime(String eaExecutiontime){
        MigrationOverheadController.eaExecutiontime += Integer.parseInt(eaExecutiontime);
        MigrationOverheadController.numberOfMigrations++;
    }
}
