package edu.kit.iai.calculation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Holds information about the island and its execution
 */
public class IslandConfig {
    //Island number, should be set as command line parameter --island.number=x
    @Value("${island.number}")
    //@Value("1")

    private String islandNumber;
    private RedisTemplate<String, Integer> template;
    private boolean stopped = false;
    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    public IslandConfig(RedisTemplate<String, Integer> template) {
        this.template = template;
    }


    public String getIslandNumber() {
        return islandNumber;
    }
}
