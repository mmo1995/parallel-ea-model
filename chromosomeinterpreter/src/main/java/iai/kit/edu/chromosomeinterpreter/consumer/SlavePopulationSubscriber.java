package iai.kit.edu.chromosomeinterpreter.consumer;

import iai.kit.edu.chromosomeinterpreter.config.ConfigResetter;
import iai.kit.edu.chromosomeinterpreter.config.ConstantStrings;
import iai.kit.edu.chromosomeinterpreter.config.IslandConfig;
import iai.kit.edu.chromosomeinterpreter.core.Chromosomeinterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;

import java.io.IOException;

/**
 * Receives the initial population
 */
public class SlavePopulationSubscriber implements MessageListener {

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;
    @Autowired
    @Qualifier("integerTemplate")
    RedisTemplate<String, Integer> intTemplate;
    @Autowired
    IslandConfig islandConfig;

     @Autowired
    Chromosomeinterpreter chromosomeiInt;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message, byte[] pattern) {

        ValueOperations<String, String> ops = this.stringRedisTemplate.opsForValue();

            String initialPopulationJson = ops.get(ConstantStrings.slavePopulation + "." + islandConfig.getIslandNumber() + "." + islandConfig.getSlaveNumber());
            try {
                //  System.out.print("I have recieved a new Chromosomes list with " + initialPopulationJson );
                chromosomeiInt.mainChromosomeInterpreter(initialPopulationJson);
            } catch (IOException e) {
                e.printStackTrace();
            }

        // System.out.println(ConstantStrings.initialPopulation + "ConstantStrings.initialPopulation");
        // System.out.println(islandConfig.getIslandNumber() + "islandConfig.getIslandNumber()");
        // System.out.println(ConstantStrings.initialPopulation + "." + islandConfig.getIslandNumber() + "ConstantStrings.initialPopulation + islandConfig.getIslandNumber()");
        //  System.out.println("initialPopulationJson  message" + initialPopulationJson);
        // System.out.println("initialPopulationJson size " + population.size());
        //    System.out.println("initialPopulationJson first one " + population.get(1).getRating());


    }
}
