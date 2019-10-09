package iai.kit.edu.consumer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.IslandConfig;
import iai.kit.edu.core.GLEAMChromosome;
import iai.kit.edu.core.MigrantReplacer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Receives migrants from neighboring islands
 */
public class MigrantSubscriber implements MessageListener {

    @Autowired
    private RedisMessageListenerContainer container;
    @Autowired
    IslandConfig islandConfig;
    @Autowired
    MigrantReplacer migrantReplacer;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message, byte[] pattern) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<GLEAMChromosome>>() {
        }.getType();
        List<GLEAMChromosome> migrants = gson.fromJson(message.toString(), listType);
        logger.debug("Received " + migrants.size() + " migrants");
        migrantReplacer.cacheMigrants(migrants);
    }

    public void subscribe() {
        ChannelTopic channelTopic;
        for (Integer neighbor : islandConfig.getNeighbors()) {
            logger.trace("subscribing neighbor "+neighbor+" with "+this);
            channelTopic = new ChannelTopic(ConstantStrings.neighborPopulation + "." + neighbor);
            container.addMessageListener(this, channelTopic);
        }
    }

    public void unsubscribe() {
        // remove this listener from all topics
        logger.trace("trying to remove "+this);
        container.removeMessageListener(this);

    }
}