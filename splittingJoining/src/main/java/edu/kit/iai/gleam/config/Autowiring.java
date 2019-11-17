package edu.kit.iai.gleam.config;

import edu.kit.iai.gleam.producer.ConfigurationAvailablePublisher;
import edu.kit.iai.gleam.producer.InitialPopulationPublisher;
import edu.kit.iai.gleam.producer.StartPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.client.RestTemplate;

/**
 * Autowires the components of the application
 */
@Configuration
@ComponentScan("edu.kit.iai.gleam")
public class Autowiring {

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
     * Creates integer template for integers stored in Redis
     * @return integer template
     */
    @Bean(name = "integerTemplate")
    public RedisTemplate<String, Integer> integerTemplate() {
        final RedisTemplate<String, Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    /**
     * Creates Redis Message Listener Container
     * @return Redist Message Listener Container
     */
    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        return container;
    }

    /**
     * Creates a start publisher
     * @return
     */
    @Bean
    StartPublisher startPublisher() {
        return new StartPublisher();
    }

    /**
     * Creates an initial population publisher
     * @return
     */
    @Bean
    InitialPopulationPublisher initialPopulationPublisher() {
        return new InitialPopulationPublisher();
    }

    /**
     * Creates configuration publisher
     */
    @Bean
    ConfigurationAvailablePublisher configurationAvailablePublisher() {
        return new ConfigurationAvailablePublisher();
    }

    @Bean(name = "startTopic")
    ChannelTopic startTopic() {
        return new ChannelTopic(ConstantStrings.managementStart);
    }

    @Bean(name = "initialPopulationTopic")
    ChannelTopic initialPopulationTopic() {
        return new ChannelTopic(ConstantStrings.initialPopulation);
    }

    @Bean(name = "configurationAvailableTopic")
    ChannelTopic configurationAvailableTopic() {
        return new ChannelTopic(ConstantStrings.managementConfig);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
}
