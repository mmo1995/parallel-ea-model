package iai.kit.edu.producer;

import com.google.gson.Gson;
import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.IslandConfig;
import iai.kit.edu.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Publishes migrants on publishing channel that is related to one island
 */
@Component
public class MigrantPublisher {

    @Autowired
    IslandConfig islandConfig;

    @Autowired
    @Qualifier("stringTemplate")
    RedisTemplate<String, String> stringTemplate;
    private int numberofMigration = 0 ; // used if the fitness is the termination criterium
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void publish(List<Chromosome> migrants) {
        publishMigrants(migrants);
    }

    private void publishMigrants(List<Chromosome> migrants) {
        ChannelTopic topic = new ChannelTopic(ConstantStrings.neighborPopulation + "." + islandConfig.getIslandNumber());
        Gson gson = new Gson();
        String migrantJson = gson.toJson(migrants);
        logger.info("starting migration");
        stringTemplate.convertAndSend(topic.getTopic(), migrantJson);
        logger.info(migrants.size() + " are migrated");
        numberofMigration++;

    }

    public int getNumberofMigration() {
        return numberofMigration;
    }

    public void setNumberofMigration(int numberofMigration) {
        this.numberofMigration = numberofMigration;
    }
}

