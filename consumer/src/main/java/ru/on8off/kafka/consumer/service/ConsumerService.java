package ru.on8off.kafka.consumer.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import ru.on8off.kafka.model.avro.PaymentEventAvro;
import ru.on8off.kafka.model.json.PaymentEventJson;
import ru.on8off.kafka.model.proto.PaymentEventProtoOuterClass;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ConsumerService {
    @Autowired
    private LinkedBlockingDeque<PaymentEventAvro> paymentEventAvros;
    @Autowired
    private LinkedBlockingDeque<PaymentEventProtoOuterClass.PaymentEventProto> paymentEventProtos;
    @Autowired
    private LinkedBlockingDeque<PaymentEventJson> paymentEventJsons;

    @KafkaListener(topics = "${topic.avro}", groupId = "${group.avro}", concurrency = "${partitions}", containerFactory = "avroListenerContainerFactory")
    public void consumeAvro(Message<PaymentEventAvro> message) throws InterruptedException {
        log.info(">> received avro: kafka_groupId={} kafka_receivedTopic={} kafka_receivedPartitionId={}, key={}, value={}",
                message.getHeaders().get(KafkaHeaders.GROUP_ID),
                message.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC),
                message.getHeaders().get(KafkaHeaders.RECEIVED_PARTITION_ID),
                message.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY),
                message.getPayload()
        );
        paymentEventAvros.offer(message.getPayload(), 100, TimeUnit.MILLISECONDS);
    }

    @KafkaListener(topics = "${topic.proto}", groupId = "${group.proto}", concurrency = "${partitions}", containerFactory = "protoListenerContainerFactory")
    public void consumeProto(Message<PaymentEventProtoOuterClass.PaymentEventProto> message) throws InterruptedException {
        log.info(">> received proto: kafka_groupId={} kafka_receivedTopic={} kafka_receivedPartitionId={}, key={}, value={}",
                message.getHeaders().get(KafkaHeaders.GROUP_ID),
                message.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC),
                message.getHeaders().get(KafkaHeaders.RECEIVED_PARTITION_ID),
                message.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY),
                message.getPayload()
        );
        paymentEventProtos.offer(message.getPayload(), 100, TimeUnit.MILLISECONDS);
    }


    @KafkaListener(topics = "${topic.json}", groupId = "${group.json}", concurrency = "${partitions}", containerFactory = "jsonListenerContainerFactory")
    public void consumeJson(Message<PaymentEventJson> message) throws InterruptedException {
        log.info(">> received json: kafka_groupId={} kafka_receivedTopic={} kafka_receivedPartitionId={}, key={}, value={}",
                message.getHeaders().get(KafkaHeaders.GROUP_ID),
                message.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC),
                message.getHeaders().get(KafkaHeaders.RECEIVED_PARTITION_ID),
                message.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY),
                message.getPayload()
        );
        paymentEventJsons.offer(message.getPayload(), 100, TimeUnit.MILLISECONDS);
    }
}
