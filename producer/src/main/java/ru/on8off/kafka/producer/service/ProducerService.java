package ru.on8off.kafka.producer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.on8off.kafka.model.avro.PaymentEventAvro;
import ru.on8off.kafka.model.json.PaymentEventJson;
import ru.on8off.kafka.model.proto.PaymentEventProtoOuterClass;

import java.util.Objects;
import java.util.Random;

@Service
@Slf4j
public class ProducerService {
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
    private GeneratorService generator;
    private Random random = new Random();

    public void produceAvro(Integer count, Integer pause) throws InterruptedException {
        count = Objects.requireNonNullElse(count,1);
        pause = Objects.requireNonNullElse(count, 1000 + random.nextInt(5) * 1000);
        for (int i = 0; i < count; i++) {
            var events = generator.generateChainForAvro();
            for (PaymentEventAvro event : events) {
                var result  = avroPaymentEventKafkaTemplate.send(topicAvro, null, System.currentTimeMillis(), event.getCustomerId(), event);
                final var n = i;
                result.addCallback(new ListenableFutureCallback<>() {
                    @Override
                    public void onSuccess(SendResult<Long, PaymentEventAvro> result) {
                        log.info(">>> produceAvro {} succeed: topic={}, key={}, eventType={}", n, topicAvro, event.getCustomerId(), event.getEventType());
                    }
                    @Override
                    public void onFailure(Throwable ex) {
                        log.info(">>> produceAvro {} onFailure: {}", n, ex.getMessage());
                    }
                });
                log.info(">>> sleep {} ms", pause);
                Thread.sleep(pause);
            }
        }
    }

    public void produceProto(Integer count, Integer pause) throws InterruptedException {
        count = Objects.requireNonNullElse(count,1);
        pause = Objects.requireNonNullElse(count, 1000 + random.nextInt(5) * 1000);
        for (int i = 0; i < count; i++) {
            var events = generator.generateChainForProto();
            for (PaymentEventProtoOuterClass.PaymentEventProto event : events) {
                var result  = protoPaymentEventKafkaTemplate.send(topicProto, null, System.currentTimeMillis(), event.getCustomerId(), event);
                final var n = i;
                result.addCallback(new ListenableFutureCallback<>() {
                    @Override
                    public void onSuccess(SendResult<Long, PaymentEventProtoOuterClass.PaymentEventProto> result) {
                        log.info(">>> produceProto {} succeed: topic={}, key={}, eventType={}", n, topicAvro, event.getCustomerId(), event.getEventType());
                    }
                    @Override
                    public void onFailure(Throwable ex) {
                        log.info(">>> produceProto {} onFailure: {}", n, ex.getMessage());
                    }
                });
                log.info(">>> sleep {} ms", pause);
                Thread.sleep(pause);
            }
        }
    }


    public void produceJson(Integer count, Integer pause) throws InterruptedException {
        count = Objects.requireNonNullElse(count,1);
        pause = Objects.requireNonNullElse(count, 1000 + random.nextInt(5) * 1000);
        for (int i = 0; i < count; i++) {
            var events = generator.generateChainForJson();
            for (PaymentEventJson event : events) {
                var result  = jsonPaymentEventKafkaTemplate.send(topicJson, null, System.currentTimeMillis(), event.getCustomerId(), event);
                final var n = i;
                result.addCallback(new ListenableFutureCallback<>() {
                    @Override
                    public void onSuccess(SendResult<Long, PaymentEventJson> result) {
                        log.info(">>> produceJson {} succeed: topic={}, key={}, eventType={}", n, topicAvro, event.getCustomerId(), event.getEventType());
                    }
                    @Override
                    public void onFailure(Throwable ex) {
                        log.info(">>> produceJson {} onFailure: {}", n, ex.getMessage());
                    }
                });
                log.info(">>> sleep {} ms", pause);
                Thread.sleep(pause);
            }
        }
    }
}
