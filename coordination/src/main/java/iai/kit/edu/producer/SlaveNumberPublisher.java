package iai.kit.edu.producer;

import iai.kit.edu.config.ConstantStrings;
import iai.kit.edu.core.Overhead;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.ChannelTopic;

public class SlaveNumberPublisher {

        @Autowired
        @Qualifier("integerTemplate")
        RedisTemplate<String, Integer> integerTemplate;
            private final Logger logger = LoggerFactory.getLogger(this.getClass());

        public void publishNumberOfSlaves(int numberOfSlaves){
            logger.info("publish number Of slaves for EA Services");

            ValueOperations<String, Integer> ops = this.integerTemplate.opsForValue();
            ops.set(ConstantStrings.numberOfSlavesTopic, numberOfSlaves);
            integerTemplate.convertAndSend(ConstantStrings.numberOfSlavesTopic, "Number of Slaves sent");
        }
}
