package ru.on8off.kafka.producer.integration.controller;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import ru.on8off.kafka.model.json.PaymentEventJson;
import ru.on8off.kafka.model.json.PaymentEventTypeJson;
import ru.on8off.kafka.model.proto.PaymentEventProtoOuterClass;
import ru.on8off.kafka.producer.integration.configuration.TestKafkaConfiguration;
import ru.on8off.kafka.model.avro.EventTypeAvro;
import ru.on8off.kafka.model.avro.PaymentEventAvro;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestKafkaConfiguration.class)
class PaymentControllerIT {
    @Autowired
    private KafkaConsumer<Long, PaymentEventAvro> avroKafkaConsumer;
    @Autowired
    private KafkaConsumer<Long, PaymentEventProtoOuterClass.PaymentEventProto> protoKafkaConsumer;
    @Autowired
    private KafkaConsumer<Long, PaymentEventJson> jsonKafkaConsumer;

    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    private String host = "http://localhost:";

    @Test
    void generateNewPaymentAvro(){
        var response = restTemplate.getForEntity(host + port + "/generateAvro?count=1&pause=10", String.class);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals("OK", response.getBody());
        var result = KafkaTestUtils.getRecords(avroKafkaConsumer);
        Assertions.assertEquals(EventTypeAvro.PaymentPageOpened, result.iterator().next().value().getEventType());
    }
    @Test
    void generateNewPaymentProto(){
        var response = restTemplate.getForEntity(host + port + "/generateProto?count=1&pause=10", String.class);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals("OK", response.getBody());

        var result = KafkaTestUtils.getRecords(protoKafkaConsumer);
        Assertions.assertEquals(PaymentEventProtoOuterClass.PaymentEventProto.EventTypeProto.PaymentPageOpened, result.iterator().next().value().getEventType());
    }

    @Test
    void generateNewPaymentJson(){
        var response = restTemplate.getForEntity(host + port + "/generateJson?count=1&pause=10", String.class);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals("OK", response.getBody());
        var result = KafkaTestUtils.getRecords(jsonKafkaConsumer);
        Assertions.assertEquals(PaymentEventTypeJson.PaymentPageOpened, result.iterator().next().value().getEventType());
    }

}