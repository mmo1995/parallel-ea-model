package iai.kit.edu.chromosomeinterpreter.producer;

import com.google.gson.Gson;
import iai.kit.edu.chromosomeinterpreter.config.ConstantStrings;

import iai.kit.edu.chromosomeinterpreter.config.IslandConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

/**
 * Publishes configuration for one epoch to the EA Service
 */
public class CalculationConfigPublisher {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IslandConfig islandConfig;

    @Autowired
    @Qualifier("calculationConfigTopic")
    ChannelTopic calculationConfigTopic;

    @Autowired
    @Qualifier("stringTemplate")
    RedisTemplate<String, String> stringTemplate;


    public void publishCalculationConfig(String populationPart) {
        ChannelTopic topic = calculationConfigTopic;
        Gson gson = new Gson();
        String populationPartJSON = gson.toJson(populationPart);
        stringTemplate.convertAndSend(topic.getTopic(),populationPartJSON);
        logger.info("the interpreted sub population is published");
    }
}
