package iai.kit.edu.controller;

import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.IslandConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * Receives the execution time of each epoch from EA and sends it with the migration
 * overhead to the coordination service
 */
@CrossOrigin(origins = "*")
public class MigrationOverheadController {

    private static long startIslandExecution = 0;


    private static long endIslandExecution = 0;

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

    public void sendExecutiontimeToCoordination(){
        String executiontime = TimeUnit.MILLISECONDS.toSeconds(endIslandExecution - startIslandExecution) + "";
        logger.info("Execution time of Island: " + executiontime + " seconds");

    }
}
