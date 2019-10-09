package iai.kit.edu.consumer;

import com.google.gson.Gson;
import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.GLEAMConfig;
import iai.kit.edu.config.IslandConfig;
import iai.kit.edu.config.MigrationConfig;
import iai.kit.edu.controller.IslandReadinessController;
import iai.kit.edu.producer.ConfigurationPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;

/**
 * Receives the configuration and distributes the relevant parts to the EA Service
 */
public class ConfigurationSubscriber implements MessageListener {
    @Autowired
    @Qualifier("stringTemplate")
    RedisTemplate<String, String> stringTemplate;
    @Autowired
    IslandConfig islandConfig;
    @Autowired
    MigrantSubscriber migrantSubscriber;
    @Autowired
    IslandReadinessController islandReadinessController;
    @Autowired
    String workspacePath;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ConfigurationPublisher configurationPublisher;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ValueOperations<String, String> ops = this.stringTemplate.opsForValue();
        String migrationConfigJson = ops.get(ConstantStrings.managementConfigMigration);
        String algorithmConfigJson = ops.get(ConstantStrings.managementConfigAlgorithm);
        String neighborsConfigJson = ops.get(ConstantStrings.managementConfigNeighbor + "." + islandConfig.getIslandNumber());
        Gson gson = new Gson();
        MigrationConfig migrationConfig = gson.fromJson(migrationConfigJson, MigrationConfig.class);
        this.islandConfig.setMigrationConfig(migrationConfig);
        configurationPublisher.publishAlgorithmConfig(algorithmConfigJson);
        GLEAMConfig gleamConfig = gson.fromJson(algorithmConfigJson, GLEAMConfig.class);
        gleamConfig.setWorkspacePath(workspacePath);
        this.islandConfig.setAlgorithmConfig(gleamConfig);
        List<String> neighborsConfig = gson.fromJson(neighborsConfigJson, List.class); // Json only has one data type for numbers -> cast double to integer
        logger.trace("neighbors: " + neighborsConfigJson);
        this.islandConfig.setNeighbors(neighborsConfig);
        logger.info("island number: " + this.islandConfig.getIslandNumber() + "/" + this.islandConfig.getMigrationConfig().getNumberOfIslands() + ", population size: " + migrationConfig.getGlobalPopulationSize());
        migrantSubscriber.subscribe();
        islandReadinessController.sendReadinessStatus(ConstantStrings.neighborsSubscribed);
    }
}