package iai.kit.edu.chromosomeinterpreter.config;

import iai.kit.edu.chromosomeinterpreter.consumer.*;
import iai.kit.edu.chromosomeinterpreter.core.*;
import iai.kit.edu.chromosomeinterpreter.producer.*;

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




/**
 * Autowires the components of the application
 */
@Configuration
@ComponentScan("iai.kit.edu.chromosomeinterpreter")
public class Autowiring {
	
    @Value("${island.number}")
    private String islandNumber;
    
    @Value("${slave.number}")
    private String slaveNumber;

    /**
     * Establishes connection to Redis
     * @return Redis connection
     */
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
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

    @Bean(name = "calculationInitializedTopic")
    ChannelTopic calculationInitializedTopic() {
        return new ChannelTopic(ConstantStrings.calculationInitialized + "." + islandNumber + "." + slaveNumber);
    }
    
    @Bean(name = "slaveInitializedTopic")
    ChannelTopic slaveInitializedtopic() {
    	return new ChannelTopic(ConstantStrings.slaveInitialized + "." + islandNumber);
    }

    @Bean(name = "slaveReadyTopic")
    ChannelTopic slaveReadytopic() {
        return new ChannelTopic(ConstantStrings.slaveReady + "." + islandNumber);
    }


    /**
     * Creates new Chromosomeinterpreter instance
     * @return
     */
    @Bean
    Chromosomeinterpreter chromosomeInter() {
        return new Chromosomeinterpreter();
    }
    /**
     * Creates listener that listens to date to start evolution
     * @return
     */

    @Bean(name = "dateListener")
    MessageListenerAdapter dateListener() {
        return new MessageListenerAdapter(dateSubscriber());
    }

    /**
     * Creates date subscriber
     * @return
     */
    @Bean
    DateSubscriber dateSubscriber() {
        return new DateSubscriber();
    }

    @Bean(name = "dateTopic")
    ChannelTopic dateTopic() {
        return new ChannelTopic(ConstantStrings.dateForScheduling);
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
     * Creates new initial population subscriber
     * @return
     */
    @Bean
    CalculationInitializedSubscriber calculationInitializedSubscriber() {
        return new CalculationInitializedSubscriber();
    }


    @Bean
    StopSubscribe stopSubscribing() {
        return new StopSubscribe();
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
     * Creates new island readiness publisher
     * @return
     */
    @Bean
    SlaveInitializedPublisher slaveInitializedPublisher() {
        return new SlaveInitializedPublisher();
    }


    @Bean
    SlaveReadinessPublisher slaveReadinessPublisher() {
        return new SlaveReadinessPublisher();
    }

    @Bean(name = "configurationTopic")
    ChannelTopic configurationTopic() {
        return new ChannelTopic(ConstantStrings.managementConfig);
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
        return new ChannelTopic(ConstantStrings.initialPopulation+ "." + islandConfig().getIslandNumber());
    }

    @Bean(name = "intermediatePopulationTopic")
    ChannelTopic intermediatePopulationTopic() {
        return new ChannelTopic(ConstantStrings.intermediatePopulation + "." + islandConfig().getIslandNumber());
    }
    @Bean(name = "stopSubschribingTopic")
    ChannelTopic stopSubschribingTopic() {
        return new ChannelTopic(ConstantStrings.stopSubscribing);
    }

    @Bean
    IslandConfig islandConfig() {
        return new IslandConfig(template());
    }


    @Bean
    ConfigResetter consumerProducerHandler() {
        return new ConfigResetter();
    }
    @Bean
    EAEpochPublisher eaEpochPublisher() {
        return new EAEpochPublisher();
    }

}
