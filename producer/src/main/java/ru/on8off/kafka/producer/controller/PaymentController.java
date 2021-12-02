package ru.on8off.kafka.producer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.on8off.kafka.producer.service.ProducerService;


@RestController
public class PaymentController {
    @Autowired
    private ProducerService producerService;

    @GetMapping("/generateAvro")
    public String generateNewPaymentAvro(@RequestParam(required = false) Integer count, @RequestParam(required = false) Integer pause) throws InterruptedException {
        producerService.produceAvro(count, pause);
        return "OK";
    }

    @GetMapping("/generateProto")
    public String generateNewPaymentProto(@RequestParam(required = false) Integer count, @RequestParam(required = false) Integer pause) throws InterruptedException {
        producerService.produceProto(count, pause);
        return "OK";
    }

    @GetMapping("/generateJson")
    public String generateNewPaymentJson(@RequestParam(required = false) Integer count, @RequestParam(required = false) Integer pause) throws InterruptedException {
        producerService.produceJson(count, pause);
        return "OK";
    }
}
