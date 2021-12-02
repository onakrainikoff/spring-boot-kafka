package ru.on8off.kafka.model.json;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;

import java.util.Objects;

@JsonInclude()
@JsonSchemaInject(strings = {@JsonSchemaString(path="javaType", value="u.on8off.kafka.model.json.PaymentEventJson")})
public class PaymentEventJson {
    @JsonProperty
    private Long timestamp;
    @JsonProperty
    private Long  customerId;
    @JsonProperty
    private PaymentEventTypeJson eventType;

    public PaymentEventJson() {
    }

    public PaymentEventJson(Long timestamp, Long customerId, PaymentEventTypeJson eventType) {
        this.timestamp = timestamp;
        this.customerId = customerId;
        this.eventType = eventType;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public PaymentEventTypeJson getEventType() {
        return eventType;
    }

    public void setEventType(PaymentEventTypeJson eventType) {
        this.eventType = eventType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentEventJson that = (PaymentEventJson) o;
        return Objects.equals(timestamp, that.timestamp) && Objects.equals(customerId, that.customerId) && eventType == that.eventType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, customerId, eventType);
    }

    @Override
    public String toString() {
        return "PaymentEventJson{" +
                "timestamp=" + timestamp +
                ", customerId=" + customerId +
                ", eventType=" + eventType +
                '}';
    }
}
