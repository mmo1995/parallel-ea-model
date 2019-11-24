package iai.kit.edu.consumer;

import com.google.gson.Gson;
import iai.kit.edu.algorithm.AlgorithmStarter;
import iai.kit.edu.config.GLEAMConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * Subscribes to algorithm specific configurations sent from Migration & Synchronization Service
 */
public class ConfigurationSubscriber implements MessageListener {
    @Autowired
    AlgorithmStarter algorithmStarter;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    String workspacePath;
    @Value("${island.number}")
    private int islandNumber;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info("received config");
        Gson gson = new Gson();
        GLEAMConfig gleamConfig = gson.fromJson(message.toString(), GLEAMConfig.class);
        gleamConfig.setWorkspacePath(workspacePath);
        gleamConfig.writeFiles();
        algorithmStarter.setIslandNumber(islandNumber);
    }
}