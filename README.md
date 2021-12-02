# spring-boot-kafka

## Run integration tests
```
mvn install -f model/pom.xml
mvn verify -Ddocker_host=localhost -f producer/pom.xml
mvn verify -Ddocker_host=localhost -f consumer/pom.xml
```

## Run Kafka
```
mvn docker:run -Ddocker_host=localhost -f producer/pom.xml
```

## Run applications
```
mvn spring-boot:run -Ddocker_host=localhost -f producer/pom.xml
mvn spring-boot:run -Ddocker_host=localhost -f consumer/pom.xml
```