package iai.kit.edu.config;

import iai.kit.edu.consumer.*;
import iai.kit.edu.controller.IslandInitializedController;
import iai.kit.edu.controller.IslandReadinessController;
import iai.kit.edu.controller.ResultController;
import iai.kit.edu.core.*;
import iai.kit.edu.producer.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * Autowires the components of the application
 */
@Configuration
@ComponentScan("iai.kit.edu")
public class Autowiring {

    /**
     * Establishes connection to Redis
     * @return Redis connection
     */
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
        //jedisConFactory.setHostName("redis");
        jedisConFactory.setHostName("localhost");
        jedisConFactory.setPort(6379);
        return jedisConFactory;
    }

    /**
     * Creates integer template for integers stored in Redis
     * @return integer template
     */
    @Bean(name = "integerTemplate")
    public RedisTemplate<String, Integer> template() {
        final RedisTemplate<String, Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());

        return template;
    }

    /**
     * Creates String template for Strings stored in Redis
     * @return String template
     */
    @Bean(name = "stringTemplate")
    public RedisTemplate<String, String> stringTemplate() {
        final RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setDefaultSerializer(new StringRedisSerializer());
        return template;
    }

    /**
     * Creates Redis Message Listener Container to interact with Redis
     * @return
     */
    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());

        return container;
    }


    /**
     * Creates new configuration subscriber
     * @return
     */
    @Bean
    ConfigurationSubscriber configurationListener() {
        return new ConfigurationSubscriber();
    }

    /**
     * Creates new start subscriber
     * @return
     */
    @Bean
    StartSubscriber startSubscriber() {
        return new StartSubscriber();
    }

    /**
     * Creates new stop subscriber
     * @return
     */
    @Bean
    StopSubscriber stopSubscriber() {
        return new StopSubscriber();
    }

    /**
     * Creates new initial population subscriber
     * @return
     */
    @Bean
    InitialPopulationSubscriber initialPopulationSubscriber() {
        return new InitialPopulationSubscriber();
    }

    /**
     * Creates new migrant subscriber
     * @return
     */
    @Bean
    MigrantSubscriber migrantSubscriber() {
        return new MigrantSubscriber();
    }

    /**
     * Creates new migration completed subscriber
     * @return
     */
    @Bean
    MigrationCompletedSubscriber migrationCompletedSubscriber() {
        return new MigrationCompletedSubscriber();
    }

    /**
     * Creates new init subscriber
     * @return
     */
    @Bean
    InitSubscriber initSubscriber() {
        return new InitSubscriber();
    }

    /**
     * Creates new intermediate population subscriber
     * @return
     */
    @Bean
    IntermediatePopulationSubscriber intermediatePopulationSubscriber() {
        return new IntermediatePopulationSubscriber();
    }

    /**
     * Creates island initialized publisher
     * @return
     */
    @Bean
    IslandInitializedController islandInitializedPublisher() {
        return new IslandInitializedController();
    }

    /**
     * Creates new island readiness publisher
     * @return
     */
    @Bean
    IslandReadinessController islandReadinessPublisher() {
        return new IslandReadinessController();
    }

    /**
     * Creates new population publisher
     * @return
     */
    @Bean
    MigrantPublisher populationPublisher() {
        return new MigrantPublisher();
    }


    @Bean
    ResultController resultController() {
        return new ResultController();
    }

    @Bean
    StopPublisher stopPublisher() {
        return new StopPublisher();
    }

    @Bean
    MigrationCompletedPublisher migrationCompletedPublisher() {
        return new MigrationCompletedPublisher();
    }

    @Bean(name = "configurationTopic")
    ChannelTopic configurationTopic() {
        return new ChannelTopic(ConstantStrings.managementConfig);
    }

    @Bean(name = "startTopic")
    ChannelTopic startTopic() {
        return new ChannelTopic(ConstantStrings.managementStart);
    }


    @Bean(name = "stopTopic")
    ChannelTopic stopTopic() {
        return new ChannelTopic(ConstantStrings.managementStop);
    }

    @Bean(name = "migrationCompletedTopic")
    ChannelTopic migrationCompletedTopic() {
        return new ChannelTopic(ConstantStrings.statusMigrationCompleted);
    }

    @Bean(name = "initializeIslandsTopic")
    ChannelTopic initializeIslandsTopic() {
        return new ChannelTopic(ConstantStrings.initializeIslands);
    }

    @Bean(name = "initialPopulationTopic")
    ChannelTopic initialPopulationTopic() {
        return new ChannelTopic(ConstantStrings.initialPopulation);
    }

    @Bean(name = "intermediatePopulationTopic")
    ChannelTopic intermediatePopulationTopic() {
        return new ChannelTopic(ConstantStrings.intermediatePopulation + "." + islandConfig().getIslandNumber());
    }

    @Bean
    IslandConfig islandConfig() {
        return new IslandConfig(template());
    }

    @Bean
    AlgorithmWrapper algorithmWrapper() {
        return new AlgorithmWrapper();
    }

    @Bean
    MigrantReplacer migrantReplacer() {
        return new MigrantReplacer();
    }

    @Bean
    MigrantSelector migrantSelector() {
        return new MigrantSelector();
    }

    @Bean
    String workspacePath() {
        return ConstantStrings.islandPath;
    }

    @Bean
    Population population() {
        return new GLEAMPopulation();
    }

    @Bean
    ConfigResetter consumerProducerHandler() {
        return new ConfigResetter();
    }

    @Bean
    ConfigurationPublisher configurationPublisher() {
        return new ConfigurationPublisher();
    }

    @Bean
    EAEpochConfig eaEpochConfig() {
        return new EAEpochConfig();
    }

    @Bean
    EAEpochPublisher eaEpochPublisher() {
        return new EAEpochPublisher();
    }
}
