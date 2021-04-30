package site.bitlab16.restservice;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import site.bitlab16.restservice.model.Gathering;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.common.quota.ClientQuotaEntity.CLIENT_ID;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    public ConsumerFactory<String, Gathering> gatheringConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, CLIENT_ID);
        props.put("auto.offset.reset", "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(Gathering.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Gathering>
    gatheringKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Gathering> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(gatheringConsumerFactory());
        factory.setBatchListener(true);
        return factory;
    }
}
