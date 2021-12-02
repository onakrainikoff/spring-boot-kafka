package ru.on8off.kafka.producer.service;

import org.springframework.stereotype.Service;
import ru.on8off.kafka.model.avro.EventTypeAvro;
import ru.on8off.kafka.model.avro.PaymentEventAvro;
import ru.on8off.kafka.model.json.PaymentEventJson;
import ru.on8off.kafka.model.json.PaymentEventTypeJson;
import ru.on8off.kafka.model.proto.PaymentEventProtoOuterClass;

import java.util.List;
import java.util.Random;

@Service
public class GeneratorService {
    private final Random random = new Random();

    public List<PaymentEventAvro> generateChainForAvro() {
        var timeStamp = System.currentTimeMillis();
        var customerId = (long) random.nextInt(100);
        return List.of(
                new PaymentEventAvro(timeStamp, customerId, EventTypeAvro.PaymentPageOpened),
                new PaymentEventAvro(timeStamp, customerId, EventTypeAvro.PaymentTypeSelected),
                new PaymentEventAvro(timeStamp, customerId, EventTypeAvro.PaymentDataFilled),
                new PaymentEventAvro(timeStamp, customerId, EventTypeAvro.PaymentSucceed)
        );
    }

    public List<PaymentEventProtoOuterClass.PaymentEventProto> generateChainForProto() {
        var timeStamp = System.currentTimeMillis();
        var customerId = (long) random.nextInt(100);
        return List.of(
                PaymentEventProtoOuterClass.PaymentEventProto
                    .newBuilder()
                    .setCustomerId(customerId)
                    .setTimestamp(timeStamp)
                    .setEventType(PaymentEventProtoOuterClass.PaymentEventProto.EventTypeProto.PaymentPageOpened)
                    .build(),
                PaymentEventProtoOuterClass.PaymentEventProto
                        .newBuilder()
                        .setCustomerId(customerId)
                        .setTimestamp(timeStamp)
                        .setEventType(PaymentEventProtoOuterClass.PaymentEventProto.EventTypeProto.PaymentTypeSelected)
                        .build(),
                PaymentEventProtoOuterClass.PaymentEventProto
                        .newBuilder()
                        .setCustomerId(customerId)
                        .setTimestamp(timeStamp)
                        .setEventType(PaymentEventProtoOuterClass.PaymentEventProto.EventTypeProto.PaymentDataFilled)
                        .build(),
                PaymentEventProtoOuterClass.PaymentEventProto
                        .newBuilder()
                        .setCustomerId(customerId)
                        .setTimestamp(timeStamp)
                        .setEventType(PaymentEventProtoOuterClass.PaymentEventProto.EventTypeProto.PaymentFailed)
                        .build()
        );
    }

    public List<PaymentEventJson> generateChainForJson() {
        var timeStamp = System.currentTimeMillis();
        var customerId = (long) random.nextInt(100);
        return List.of(
                new PaymentEventJson(timeStamp, customerId, PaymentEventTypeJson.PaymentPageOpened),
                new PaymentEventJson(timeStamp, customerId, PaymentEventTypeJson.PaymentTypeSelected),
                new PaymentEventJson(timeStamp, customerId, PaymentEventTypeJson.PaymentDataFilled),
                new PaymentEventJson(timeStamp, customerId, PaymentEventTypeJson.PaymentSucceed)
        );
    }
}
