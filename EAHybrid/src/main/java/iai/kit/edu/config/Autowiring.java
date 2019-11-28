package iai.kit.edu.config;

import iai.kit.edu.algorithm.AlgorithmStarter;
import iai.kit.edu.algorithm.GLEAMStarter;
import iai.kit.edu.consumer.ConfigurationSubscriber;
import iai.kit.edu.consumer.EAEpochSubscriber;
import iai.kit.edu.producer.IntermediatePopulationPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.File;


/**
 * Autowires the components of the application
 */
@Configuration
@ComponentScan("iai.kit.edu")
public class Autowiring {

    /**
     * Value should be set through command line parameter --island.number=X
     */
    @Value("${island.number}")
    private int islandNumber;

    /**
     * Creates workspace path
     * @return
     */
    @Bean
    String workspacePath() {
        return ConstantStrings.islandPath;
    }

    /**
     * Creates algorithmStarter
     * @return
     */
    @Bean
    AlgorithmStarter algorithmStarter() {
        return new GLEAMStarter(workspacePath());
    }
    

    /**
     * Creates Redis Message Listener Container to establish subscribers to intermediate population channel
     * @return intermediate population message listener container
     */
    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(eaEpochListener(), eaEpochTopic());
        container.addMessageListener(eaConfigurationListener(), eaConfigTopic());
        return container;
    }


    /**
     * Establishes connection to Redis
     * @return Redis connection
     */
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
        // jedisConFactory.setHostName("redis");
        jedisConFactory.setHostName("localhost");
        jedisConFactory.setPort(6379);
        return jedisConFactory;
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
     * Creates listener that listens to corresponding Migration & Synchronization Service to start epochs
     * @return
     */
    @Bean
    MessageListenerAdapter eaEpochListener() {
        return new MessageListenerAdapter(eaEpochSubscriber());
    }

    /**
     * Creates corresponding subscriber
     * @return
     */
    @Bean
    EAEpochSubscriber eaEpochSubscriber() {
        return new EAEpochSubscriber();
    }

    @Bean
    ChannelTopic eaEpochTopic() {
        return new ChannelTopic(ConstantStrings.epochTopic + "." + islandNumber);
    }

    /**
     * Listens to configuration for one epoch
     * @return
     */
    @Bean
    MessageListenerAdapter eaConfigurationListener() {
        return new MessageListenerAdapter(eaConfigurationSubscriber());
    }
    @Bean
    ConfigurationSubscriber eaConfigurationSubscriber() {
        return new ConfigurationSubscriber();
    }

    @Bean
    ChannelTopic eaConfigTopic() {
        return new ChannelTopic(ConstantStrings.EAConfig + "." + islandNumber);
    }

    /**
     * File path to intermediate population
     * @return
     */
    @Bean
    File populationFile() {
        return new File(ConstantStrings.islandPath + ConstantStrings.intermediatePopulationFileName);
    }

    @Bean
    File populationIntialFile() {
        return new File(ConstantStrings.islandPath + ConstantStrings.initialPopulationFileName);
    }

    /**
     * Publishes intermediate population to corresponding Migration & Synchronization Service
     * @return
     */
    @Bean
    IntermediatePopulationPublisher intermediatePopulationPublisher(){
        return new IntermediatePopulationPublisher();
    }

}