package edu.kit.iai.calculation.config;

import edu.kit.iai.calculation.consumer.CalculationConfigSubscriber;
import edu.kit.iai.calculation.consumer.DateSubscriber;
import edu.kit.iai.calculation.consumer.InitSubscriber;
import edu.kit.iai.calculation.consumer.StopSubscribe;
import edu.kit.iai.calculation.core.Calculation;
import edu.kit.iai.calculation.producer.CalculationInitializedPublisher;
import edu.kit.iai.calculation.producer.IntermediatePopulationPublisher;
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
import redis.clients.jedis.Protocol;


/**
 * Autowires the components of the application
 */
@Configuration
@ComponentScan("edu.kit.iai.calculation")
public class Autowiring {

    /**
     * Value should be set through command line parameter --island.number=X
     */
    @Value("${island.number}")
    private String islandNumber;
    
    @Value("${slave.number}")
    private String slaveNumber;



    /**
     * Creates Redis Message Listener Container to establish subscribers to intermediate population channel
     * @return intermediate population message listener container
     */
    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
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
        jedisConFactory.setTimeout(100000);
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
     * Creates listener that listens to corresponding chromosome Interpreter Service to start epochs
     * @return
     */
    @Bean(name = "calculationConfigListener")
    MessageListenerAdapter calculationConfigListener() {
        return new MessageListenerAdapter(calculationConfigSubscriber());
    }

    /**
     * Creates population subscriber
     * @return
     */
    @Bean
    CalculationConfigSubscriber calculationConfigSubscriber() {
        return new CalculationConfigSubscriber();
    }

    @Bean(name = "calculationConfigTopic")
    ChannelTopic calculationConfigTopic() {
        return new ChannelTopic(ConstantStrings.calculationConfigTopic + "." + islandNumber + "." + slaveNumber);
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

    /**
     * Creates new init subscriber
     * @return
     */
    @Bean
    InitSubscriber initSubscriber() {
        return new InitSubscriber();
    }

    @Bean(name = "initializeCalculationTopic")
    ChannelTopic initializeCalculationTopic() {
        return new ChannelTopic(ConstantStrings.initializeCalculation);
    }
    
    @Bean(name = "calculationInitializedTopic")
    ChannelTopic calculationInitializedTopic() {
        return new ChannelTopic(ConstantStrings.calculationInitialized + "." + islandNumber + "." + slaveNumber);
    }

    @Bean(name = "dateTopic")
    ChannelTopic dateTopic() {
        return new ChannelTopic(ConstantStrings.dateForScheduling);
    }

    @Bean(name = "stopSubschribingTopic")
    ChannelTopic stopSubschribingTopic() {
        return new ChannelTopic(ConstantStrings.stopSubscribing);
    }


    @Bean
    IslandConfig islandConfig() {
        return new IslandConfig(template());
    }


    /**
     * Publishes intermediate population to corresponding Migration & Synchronization Service
     * @return
     */
    @Bean
    IntermediatePopulationPublisher intermediatePopulationPublisher(){
        return new IntermediatePopulationPublisher();
    }


    /**
     * Publishes intermediate population to corresponding Migration & Synchronization Service
     * @return
     */
    @Bean
    CalculationInitializedPublisher calculationInitializedPublisher(){
        return new CalculationInitializedPublisher();
    }
    /**
     * Creates new calculation instance
     * @return
     */
    @Bean
    Calculation calculation(){
        return new Calculation();
    }
    @Bean
    ConfigResetter consumerProducerHandler() {
        return new ConfigResetter();
    }
    @Bean
    StopSubscribe stopSubscribing() {
        return new StopSubscribe();
    }
}