package iai.kit.edu.config;

import iai.kit.edu.consumer.*;
import iai.kit.edu.controller.IslandInitializedController;
import iai.kit.edu.core.AlgorithmWrapper;
import iai.kit.edu.core.Population;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * Resets the config between two succeeding optimization tasks
 */
public class ConfigResetter {

    @Autowired
    RedisMessageListenerContainer container;
    @Autowired
    InitSubscriber initSubscriber;
    @Autowired
    EAReadinessSubscriber eaReadinessSubscriber;
    @Autowired
    ConfigurationSubscriber configurationSubscriber;
    @Autowired
    DynamicConfigurationSubscriber dynamicConfigurationSubscriber;
    @Autowired
    StopSubscriber stopSubscriber;
    @Autowired
    StartSubscriber startSubscriber;
    @Autowired
    InitialPopulationSubscriber initialPopulationSubscriber;
    @Autowired
    MigrationCompletedSubscriber migrationCompletedSubscriber;
    @Autowired
    @Qualifier("initializeIslandsTopic")
    ChannelTopic initializeIslandsTopic;
    @Autowired
    @Qualifier("configurationTopic")
    ChannelTopic configurationTopic;
    @Autowired
    @Qualifier("dynamicConfigurationTopic")
    ChannelTopic dynamicConfigurationTopic;
    @Autowired
    @Qualifier("stopTopic")
    ChannelTopic stopTopic;
    @Autowired
    @Qualifier("startTopic")
    ChannelTopic startTopic;
    @Autowired
    @Qualifier("eaReadyTopic")
    ChannelTopic eaReadyTopic;
    @Autowired
    @Qualifier("initialPopulationTopic")
    ChannelTopic initialPopulationTopic;
    @Autowired
    @Qualifier("migrationCompletedTopic")
    ChannelTopic migrationCompletedTopic;
    @Autowired
    @Qualifier("intermediatePopulationTopic")
    ChannelTopic intermediatePopulationTopic;
    @Autowired
    IslandInitializedController islandInitializedController;
    @Autowired
    IslandConfig islandConfig;
    @Autowired
    AlgorithmWrapper algorithmWrapper;
    @Autowired
    MigrantSubscriber migrantSubscriber;
    @Autowired
    IntermediatePopulationSubscriber intermediatePopulationSubscriber;
    @Autowired
    Population population;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void initialize() {
        container.addMessageListener(initSubscriber, initializeIslandsTopic);
        container.addMessageListener(configurationSubscriber, configurationTopic);
        container.addMessageListener(dynamicConfigurationSubscriber, dynamicConfigurationTopic);
        container.addMessageListener(stopSubscriber, stopTopic);
        container.addMessageListener(startSubscriber, startTopic);
        container.addMessageListener(eaReadinessSubscriber, eaReadyTopic);
        container.addMessageListener(initialPopulationSubscriber, initialPopulationTopic);
        container.addMessageListener(migrationCompletedSubscriber, migrationCompletedTopic);
        container.addMessageListener(intermediatePopulationSubscriber, intermediatePopulationTopic);
    }

    public void reset() {
        logger.info("resetting island");
        container.removeMessageListener(configurationSubscriber, configurationTopic);
        container.removeMessageListener(dynamicConfigurationSubscriber, dynamicConfigurationTopic);
        container.removeMessageListener(stopSubscriber, stopTopic);
        container.removeMessageListener(startSubscriber, startTopic);
        container.removeMessageListener(eaReadinessSubscriber, eaReadyTopic);
        container.removeMessageListener(initialPopulationSubscriber, initialPopulationTopic);
        container.removeMessageListener(migrationCompletedSubscriber, migrationCompletedTopic);
        container.removeMessageListener(intermediatePopulationSubscriber, intermediatePopulationTopic);
        islandConfig.reset();
        algorithmWrapper.reset();
        migrantSubscriber.unsubscribe();
        population.clear();
    }
}
