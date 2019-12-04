package iai.kit.edu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import iai.kit.edu.producer.CalculationInitializedPublisher;



/**
 * Autowires the components of the application
 */
@Configuration
@ComponentScan("iai.kit.edu")
public class Autowiring {
	

	
    /**
     * Creates new calculation initalized publisher
     * @return
     */
    @Bean
    CalculationInitializedPublisher calculationInitializedPublisher() {
        return new CalculationInitializedPublisher();
    }
    
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

}
