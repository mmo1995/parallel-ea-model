package iai.kit.edu.config;

import iai.kit.edu.consumer.IntermediatePopulationSubscriber;
import iai.kit.edu.controller.InitializerEAController;
import iai.kit.edu.core.AlgorithmManager;
import iai.kit.edu.producer.SlaveNumberPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;


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
     * Creates Redistemplate to communicate with Redis
     * @return template for communication
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
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
     * Creates String template for Strings stored in Redis
     * @return String template
     */
    @Bean(name = "stringTemplate")
    public RedisTemplate<String, String> stringTemplate() {
        final RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());

        return template;
    }

    /**
     * Creates listener for intermediate population
     * @return intermediate population listener
     */
    @Bean
    MessageListenerAdapter intermediatePopulationListener() {
        return new MessageListenerAdapter(intermediatePopulationSubscriber());
    }

    /**
     * Creates the intermediate population subscriber needed by the message listener
     * @return intermediate population subscriber
     */
    @Bean
    IntermediatePopulationSubscriber intermediatePopulationSubscriber() {
        return new IntermediatePopulationSubscriber();
    }

    /**
     * Creates Redis Message Listener Container to establish subscribers to intermediate population channel
     * @return intermediate population message listener container
     */
    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(intermediatePopulationListener(), intermediatePopulationTopic());
        return container;
    }

    /**
     * Creates intermediate population pubsub topic
     * @return
     */
    @Bean
    ChannelTopic intermediatePopulationTopic() {
        return new ChannelTopic(ConstantStrings.intermediatePopulation);
    }

    /**
     * Returns initializer workspace path
     * @return workspace path
     */
    @Bean
    String workspacePath() {
        return ConstantStrings.initializerPath;
    }


    /**
     * Creates database cleaner
     * @return database cleaner
     */
    @Bean
    CounterResetter databaseCleaner() {
        return new CounterResetter();
    }

    /**
     * Creates new SlaveNumberPublisher
     * @return new SlaveNumberPublisher
     */
    @Bean
    SlaveNumberPublisher slaveNumberPublisher(){
        return new SlaveNumberPublisher();
    }

    /**
     * Creates empty jobconfig
     * @return job configuration
     */
    @Bean
    JobConfig jobConfig() {
        return new JobConfig();
    }

    /**
     * Creates new algorithm manager
     * @return algorithm manager
     */
    @Bean
    AlgorithmManager algorithmManager() {
        return new AlgorithmManager();
    }

    /**
     * Creates InitializerEAController
     * @return
     */
    @Bean
    InitializerEAController initializerEAController() {
        return new InitializerEAController();
    }

}
