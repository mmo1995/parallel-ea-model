package iai.kit.edu.controller;

import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.JobConfig;
import iai.kit.edu.core.AlgorithmManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.web.bind.annotation.*;

/**
 * Receives information about the initialization status of the islands, if all initialized then it sends the
 * configuration
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ojm")
public class IslandInitializedController {
    @Autowired
    @Qualifier("integerTemplate")
    RedisTemplate<String, Integer> template;
    @Autowired
    private AlgorithmManager algorithmManager;
    @Autowired
    private JobConfig jobConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/island_status/initialized", method = RequestMethod.POST)
    public void receiveIslandStatusInitialized(@RequestBody int islandNumber) {
        logger.debug("island " + islandNumber + " initialized");
        RedisAtomicInteger initializedIslandCounter = new RedisAtomicInteger(ConstantStrings.initializedIslandCounter, template.getConnectionFactory());
        int initializedIslands = initializedIslandCounter.incrementAndGet();
        logger.debug("islands initialized: "+initializedIslands);
        if (initializedIslands == jobConfig.getNumberOfIslands()) {
            logger.info("islands initialized");
            algorithmManager.sendConfig();
        }
    }

}
