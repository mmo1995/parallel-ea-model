package iai.kit.edu.controller;

import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.config.JobConfig;
import iai.kit.edu.core.AlgorithmManager;
import iai.kit.edu.core.Overhead;
import iai.kit.edu.producer.SlaveNumberPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * Receives information about the initialization status of the slaves, if all initialized then it sends the
 * configuration
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ojm")
public class SlavesInitializedController {
    @Autowired
    @Qualifier("integerTemplate")
    RedisTemplate<String, Integer> template;
    @Autowired
    private AlgorithmManager algorithmManager;
    @Autowired
    private SlaveNumberPublisher slaveNumberPublisher;
    @Autowired
    private JobConfig jobConfig;
    @Autowired
    private Overhead overhead;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/slaves/ready", method = RequestMethod.POST)
    public void receiveIslandStatusInitialized(@RequestBody String numberOfReadySlaves) {
        logger.info("The "+ numberOfReadySlaves +" required slaves are ready");
        overhead.setEndSlaveCreation(System.currentTimeMillis());
        logger.info("Slaves creation duration " + TimeUnit.MILLISECONDS.toSeconds(overhead.getEndSlaveCreation() - overhead.getStartSlaveCreation()));
    }

}
