package iai.kit.edu.producer;

import com.google.gson.Gson;
import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.core.Population;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

import java.util.concurrent.TimeUnit;

/**
 * Publishes EA Exeutiontime to Migration Synchronization Service
 */
public class EAExecutiontimePublisher {


    @Value("${island.number}")
    private int islandNumber;

    @Autowired
    @Qualifier("stringTemplate")
    RedisTemplate<String, String> stringTemplate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static long startEAExecution = 0;
    private static long endEAExecution = 0;

    public static long getStartEAExecution() {
        return startEAExecution;
    }

    public static void setStartEAExecution(long startEAExecution) {
        EAExecutiontimePublisher.startEAExecution = startEAExecution;
    }

    public static long getEndEAExecution() {
        return endEAExecution;
    }
    public static void setEndEAExecution(long endEAExecution) {
        EAExecutiontimePublisher.endEAExecution = endEAExecution;
    }
    public void publishEAExecutiontime(){
        String executionTime = TimeUnit.MILLISECONDS.toSeconds(endEAExecution - startEAExecution) + "";
        logger.info("execution time: " + executionTime + " seconds");
        logger.debug("publishing execution time to Migration Service");
        ChannelTopic eaExecutiontimeTopic = new ChannelTopic(ConstantStrings.eaExecutiontime+"."+islandNumber);
        stringTemplate.convertAndSend(eaExecutiontimeTopic.getTopic(), executionTime);
        setEndEAExecution(0);
        setStartEAExecution(0);
    }

}
