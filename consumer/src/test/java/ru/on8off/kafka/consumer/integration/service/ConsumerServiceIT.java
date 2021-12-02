package ru.on8off.kafka.consumer.integration.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import ru.on8off.kafka.consumer.integration.configuration.TestKafkaConfiguration;
import ru.on8off.kafka.consumer.service.ConsumerService;
import ru.on8off.kafka.model.avro.EventTypeAvro;
import ru.on8off.kafka.model.avro.PaymentEventAvro;
import ru.on8off.kafka.model.json.PaymentEventJson;
import ru.on8off.kafka.model.json.PaymentEventTypeJson;
import ru.on8off.kafka.model.proto.PaymentEventProtoOuterClass;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;


@SpringBootTest
@Import(TestKafkaConfiguration.class)
class ConsumerServiceIT {
    @Value("${topic.avro}")
    private String topicAvro;
    @Value("${topic.proto}")
    private String topicProto;
    @Value("${topic.json}")
    private String topicJson;
    @Autowired
    private KafkaTemplate<Long, PaymentEventAvro> avroPaymentEventKafkaTemplate;
    @Autowired
    private KafkaTemplate<Long, PaymentEventProtoOuterClass.PaymentEventProto> protoPaymentEventKafkaTemplate;
    @Autowired
    private KafkaTemplate<Long, PaymentEventJson> jsonPaymentEventKafkaTemplate;
    @Autowired
    private ConsumerService consumerService;
    @Autowired
    private LinkedBlockingDeque<PaymentEventAvro> paymentEventAvros;
    @Autowired
    private LinkedBlockingDeque<PaymentEventProtoOuterClass.PaymentEventProto> paymentEventProtos;
    @Autowired
    private LinkedBlockingDeque<PaymentEventJson> paymentEventJsons;


    @Test
    void consumeAvro() throws ExecutionException, InterruptedException {
        var event = new PaymentEventAvro(1L,2L, EventTypeAvro.PaymentPageOpened);
        avroPaymentEventKafkaTemplate.send(topicAvro, event.getCustomerId(), event).get();
        var result = paymentEventAvros.take();
        Assertions.assertEquals(1L, result.getTimestamp());
        Assertions.assertEquals(2L, result.getCustomerId());
        Assertions.assertEquals(EventTypeAvro.PaymentPageOpened, result.getEventType());
    }

    @Test
    void consumeProto() throws ExecutionException, InterruptedException {
        var event =  PaymentEventProtoOuterClass.PaymentEventProto
                .newBuilder()
                .setCustomerId(2L)
                .setTimestamp(1L)
                .setEventType(PaymentEventProtoOuterClass.PaymentEventProto.EventTypeProto.PaymentPageOpened)
                .build();
        protoPaymentEventKafkaTemplate.send(topicProto, event.getCustomerId(), event).get();
        var result = paymentEventProtos.take();
        Assertions.assertEquals(1L, result.getTimestamp());
        Assertions.assertEquals(2L, result.getCustomerId());
        Assertions.assertEquals(PaymentEventProtoOuterClass.PaymentEventProto.EventTypeProto.PaymentPageOpened, result.getEventType());
    }

    @Test
    void consumeJson() throws ExecutionException, InterruptedException {
        var event = new PaymentEventJson(1L,2L, PaymentEventTypeJson.PaymentPageOpened);
        jsonPaymentEventKafkaTemplate.send(topicJson, event.getCustomerId(), event).get();
        var result = paymentEventJsons.take();
        Assertions.assertEquals(1L, result.getTimestamp());
        Assertions.assertEquals(2L, result.getCustomerId());
        Assertions.assertEquals(PaymentEventTypeJson.PaymentPageOpened, result.getEventType());
    }
}