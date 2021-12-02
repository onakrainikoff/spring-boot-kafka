package ru.on8off.kafka.producer.configuration;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.serializers.KafkaJsonSerializer;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializer;
import io.confluent.kafka.serializers.subject.RecordNameStrategy;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
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
    @Value("${topic.json}")
    private String topicJson;
    @Value("${topic.proto}")
    private String topicProto;
    @Value("${partitions}")
    private Integer partitions;
    @Value("${replications}")
    private Short replications;

    @Bean
    public KafkaTemplate<Long, PaymentEventAvro> avroPaymentEventKafkaTemplate(){
        var properties = commonConfiguration();
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        properties.put(KafkaAvroSerializerConfig.VALUE_SUBJECT_NAME_STRATEGY, RecordNameStrategy.class);
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(properties));
    }

    @Bean
    public KafkaTemplate<Long, PaymentEventProtoOuterClass.PaymentEventProto> protoPaymentEventKafkaTemplate(){
        var properties = commonConfiguration();
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaProtobufSerializer.class);
        properties.put(KafkaAvroSerializerConfig.VALUE_SUBJECT_NAME_STRATEGY, RecordNameStrategy.class);
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(properties));
    }

    @Bean
    public KafkaTemplate<Long, PaymentEventJson> jsonPaymentEventKafkaTemplate(){
        var properties = commonConfiguration();
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class);
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(properties));
    }

    public Map<String, Object> commonConfiguration(){
        var properties = new HashMap<String, Object>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 1000);
        properties.put(ProducerConfig.RETRIES_CONFIG, 0);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 10000);
        properties.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
        properties.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, true);
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
