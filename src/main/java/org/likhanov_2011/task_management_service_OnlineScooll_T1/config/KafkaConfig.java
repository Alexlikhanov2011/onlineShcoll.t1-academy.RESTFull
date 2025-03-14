package org.likhanov_2011.task_management_service_OnlineScooll_T1.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskStatusUpdateDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.likhanov_2011.task_management_service_OnlineScooll_T1.dto.TaskDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;


import java.util.HashMap;
import java.util.Map;


@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.topics.task-updates}")
    private String taskUpdatesTopic;

    @Value("${spring.kafka.topics.status-updates}")
    private String statusUpdatesTopic;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public NewTopic taskUpdatesTopic() {
        return new NewTopic(taskUpdatesTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic taskStatusUpdatesTopic() {
        return new NewTopic(statusUpdatesTopic, 1, (short) 1);
    }

    @Bean
    public ProducerFactory<String, TaskDTO> taskDTOProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, TaskDTO> taskDTOKafkaTemplate() {
        return new KafkaTemplate<>(taskDTOProducerFactory());
    }

    @Bean
    public ProducerFactory<String, TaskStatusUpdateDTO> taskStatusUpdateProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, TaskStatusUpdateDTO> taskStatusUpdateKafkaTemplate() {
        return new KafkaTemplate<>(taskStatusUpdateProducerFactory());
    }

    @Bean
    public ConsumerFactory<String, TaskDTO> taskDTOConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, TaskDTO.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaskDTO> taskDTOKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TaskDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(taskDTOConsumerFactory());
        return factory;
    }
}
