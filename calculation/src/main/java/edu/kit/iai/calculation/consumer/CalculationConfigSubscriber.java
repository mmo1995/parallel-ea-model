package edu.kit.iai.calculation.consumer;

import com.google.gson.JsonArray;
import edu.kit.iai.calculation.config.IslandConfig;
import com.google.gson.Gson;
import edu.kit.iai.calculation.config.ConstantStrings;
import edu.kit.iai.calculation.core.Calculation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;

/**
 * Receives configuration for one epoch
 */
public class CalculationConfigSubscriber implements MessageListener {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;
    @Autowired
    IslandConfig islandConfig;

    @Autowired
    Calculation calculation;


    /**
     * After receiving the configuration for one epoch, the algorithmStarter is configured and afterwards
     * one epoch executed and the intermediate population is sent back to Migration & Synchronization Service
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {

        Gson gson = new Gson();
        String partPopulationJson=  gson.fromJson(message.toString(), String.class);
        String idNumber = partPopulationJson.substring(partPopulationJson.indexOf("#")+1);
        partPopulationJson = partPopulationJson.substring(0, partPopulationJson.indexOf("#"));
        JsonArray partPopulationJsonAsJsonArray = gson.fromJson(partPopulationJson, JsonArray.class);
        logger.info("received a sub population with  " + partPopulationJsonAsJsonArray.size()+ " chromosomes");

        // to execute calculation
        calculation.calculationPrice(partPopulationJsonAsJsonArray, idNumber);

    }


}
