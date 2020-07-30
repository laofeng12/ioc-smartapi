package org.openjava.boot.conf;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义kafka配置
 */
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private Boolean enableAutoCommit;
//    @Value("${spring.kafka.consumer.auto-commit-interval}")
//    private Integer autoCommitInterval;
    @Value("${spring.kafka.consumer.max-poll-records}")
    private Integer maPollRecords;


    //Kafka配置
    private Map<String, Object> consumerProperties(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        //props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maPollRecords);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    /**
     * 不使用spring boot默认方式创建的DefaultKafkaConsumerFactory，重新定义创建方式
     * @return
     */
    @Bean("consumerFactory")
    public DefaultKafkaConsumerFactory consumerFactory(){
        return new DefaultKafkaConsumerFactory(consumerProperties());
    }

    //个性化定义消费者
    @Bean("batchContainerFactory")
    public ConcurrentKafkaListenerContainerFactory batchContainerFactory(DefaultKafkaConsumerFactory consumerFactory) {
        //指定使用DefaultKafkaConsumerFactory
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory);
        //手动提交偏移量
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setBatchListener(true);
        return factory;
    }

}
