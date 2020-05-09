package iai.kit.edu.config;

import iai.kit.edu.consumer.InitSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * Resets the config between two succeeding optimization tasks
 */
public class ConfigResetter {

    @Autowired
    RedisMessageListenerContainer container;
    @Autowired
    InitSubscriber initSubscriber;
    @Autowired
    MessageListenerAdapter eaEpochListener;
    @Autowired
    MessageListenerAdapter eaConfigurationListener;
    @Autowired
    MessageListenerAdapter slaveInitializedListener;
    @Autowired
    MessageListenerAdapter slaveReadinessListener;
    @Autowired
    MessageListenerAdapter numberOfSlavesListener;

    @Autowired
    @Qualifier("initializeEATopic")
    ChannelTopic initializeEATopic;
    @Autowired
    @Qualifier("eaEpochTopic")
    ChannelTopic eaEpochTopic;
    @Autowired
    @Qualifier("eaConfigTopic")
    ChannelTopic eaConfigTopic;
    @Autowired
    @Qualifier("slaveInitializedTopic")
    ChannelTopic slaveInitializedTopic;
    @Autowired
    @Qualifier("slaveReadyTopic")
    ChannelTopic slaveReadyTopic;
    @Autowired
    @Qualifier("numberOfSlavesTopic")
    ChannelTopic numberOfSlavesTopic;




    public void initialize() {
        container.addMessageListener(initSubscriber, initializeEATopic);
        container.addMessageListener(eaEpochListener, eaEpochTopic);
        container.addMessageListener(eaConfigurationListener, eaConfigTopic);
        container.addMessageListener(slaveInitializedListener, slaveInitializedTopic);
        container.addMessageListener(slaveReadinessListener, slaveReadyTopic);
        container.addMessageListener(numberOfSlavesListener, numberOfSlavesTopic);
    }
    /*@Override
    public void run(ApplicationArguments applicationArguments) {
        this.initialize();
    }*/
}
