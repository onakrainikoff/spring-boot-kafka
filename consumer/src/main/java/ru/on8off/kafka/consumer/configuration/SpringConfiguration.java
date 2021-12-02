package ru.on8off.kafka.consumer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.on8off.kafka.model.avro.PaymentEventAvro;
import ru.on8off.kafka.model.json.PaymentEventJson;
import ru.on8off.kafka.model.proto.PaymentEventProtoOuterClass;

import java.util.concurrent.LinkedBlockingDeque;

@Configuration
public class SpringConfiguration {

    @Bean
    public LinkedBlockingDeque<PaymentEventAvro> paymentEventAvros(){
        return new LinkedBlockingDeque<>();
    }

    @Bean
    public LinkedBlockingDeque<PaymentEventProtoOuterClass.PaymentEventProto> paymentEventProtos(){
        return new LinkedBlockingDeque<>();
    }

    @Bean
    public LinkedBlockingDeque<PaymentEventJson> paymentEventJsons(){
        return new LinkedBlockingDeque<>();
    }
}
