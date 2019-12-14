package iai.kit.edu.chromosomeinterpreter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds information about the island and its execution
 */
public class IslandConfig {
    //Island number, should be set as command line parameter --island.number=x
	@Value("${island.number}")
    private String islandNumber;
    
	@Value("${slave.number}")
    private String slaveNumber;
    
    public String getSlaveNumber() {
		return slaveNumber;
	}


	public void setSlaveNumber(String slaveNumber) {
		this.slaveNumber = slaveNumber;
	}


	private RedisTemplate<String, Integer> template;

    public IslandConfig(RedisTemplate<String, Integer> template) {
        this.template = template;
    }


    public String getIslandNumber() {
        return islandNumber;
    }
    

}
