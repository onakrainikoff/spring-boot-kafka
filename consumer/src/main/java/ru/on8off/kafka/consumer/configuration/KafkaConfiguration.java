package ru.on8off.kafka.consumer.configuration;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaJsonDeserializer;
import io.confluent.kafka.serializers.KafkaJsonDeserializerConfig;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializerConfig;
import io.confluent.kafka.serializers.subject.RecordNameStrategy;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import ru.on8off.kafka.model.avro.PaymentEventAvro;
import ru.on8off.kafka.model.json.PaymentEventJson;
import ru.on8off.kafka.model.proto.PaymentEventProtoOuterClass;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {
    @Value("${bootstrap-servers}")
    private String bootstrapServers;
    @Value("${schema-registry}")
    private String schemaRegistry;
    @Value("${topic.avro}")
    private String topicAvro;
    @Value("${topic.proto}")
    private String topicProto;
    @Value("${topic.json}")
    private String topicJson;
    @Value("${partitions}")
    private Integer partitions;
    @Value("${replications}")
    private Short replications;
    @Value("${group.avro}")
    private String groupIdAvro;
    @Value("${group.proto}")
    private String groupIdProto;
    @Value("${group.json}")
    private String groupIdJson;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, PaymentEventAvro> avroListenerContainerFactory() {
        var properties = getCommonConfig();
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupIdAvro);
        properties.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        var factory = new ConcurrentKafkaListenerContainerFactory<Long, PaymentEventAvro> ();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(properties));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, PaymentEventProtoOuterClass.PaymentEventProto> protoListenerContainerFactory() {
        var properties = getCommonConfig();
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaProtobufDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupIdProto);
        properties.put(KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE, PaymentEventProtoOuterClass.PaymentEventProto.class);
        var factory = new ConcurrentKafkaListenerContainerFactory<Long, PaymentEventProtoOuterClass.PaymentEventProto> ();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(properties));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, PaymentEventJson> jsonListenerContainerFactory() {
        var properties = getCommonConfig();
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaJsonDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupIdJson);
        properties.put(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, PaymentEventJson.class);
        var factory = new ConcurrentKafkaListenerContainerFactory<Long, PaymentEventJson> ();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(properties));
        return factory;
    }

    private Map<String, Object> getCommonConfig(){
        var properties = new HashMap<String, Object>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        properties.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
        properties.put(KafkaAvroDeserializerConfig.AUTO_REGISTER_SCHEMAS, true);
        properties.put(KafkaAvroDeserializerConfig.VALUE_SUBJECT_NAME_STRATEGY, RecordNameStrategy.class);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return properties;
    }

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topicAvro() {
        return new NewTopic(topicAvro, partitions, replications);
    }
    @Bean
    public NewTopic topicProto() {
        return new NewTopic(topicProto, partitions, replications);
    }
    @Bean
    public NewTopic topicJson() {
        return new NewTopic(topicJson, partitions, replications);
    }
}
