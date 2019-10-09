package iai.kit.edu.controller;

import iai.kit.edu.config.ConfigResetter;
import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.IslandConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

/**
 * Sends initialization status to Coordination Service
 */
@CrossOrigin(origins = "*")
public class IslandInitializedController implements ApplicationRunner {

    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    IslandConfig islandConfig;
    @Autowired
    RedisMessageListenerContainer redisMessageListenerContainer;
    @Autowired
    ConfigResetter configResetter;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void sendInitializedStatus() {
        ResponseEntity<String> answer1 = restTemplate.postForEntity(ConstantStrings.coordinationURL + "/ojm/island_status/initialized/", islandConfig.getIslandNumber(), String.class);
    }

    @Override
    public void run(ApplicationArguments applicationArguments) {
        configResetter.initialize();
        this.sendInitializedStatus();
    }
}
