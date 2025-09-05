package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class TaskTwoTests {
    static final Logger logger = LoggerFactory.getLogger(TaskTwoTests.class);

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private KafkaTemplate<String, Transaction> originalKafkaTemplate;

    @Value("${spring.embedded.kafka.brokers}")
    private String embeddedBrokers;

    @BeforeEach
    void setup() {
        // Override KafkaTemplate to point to embedded Kafka broker
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedBrokers);
        KafkaTemplate<String, Transaction> testTemplate =
                new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerProps));

        // Replace the KafkaProducer's template with the test template
        kafkaProducer = new KafkaProducer("transactions", testTemplate);
    }

    @Test
    void task_two_verifier() throws InterruptedException {
        // Load test transactions
        String[] transactionLines = fileLoader.loadStrings("/test_data/poiuytrewq.uiop");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        // Wait briefly for transactions to be consumed
        Thread.sleep(2000);

        logger.info("----------------------------------------------------------");
        logger.info("----------------------------------------------------------");
        logger.info("----------------------------------------------------------");
        logger.info("Use your debugger to watch for incoming transactions");
        logger.info("Kill this test once you have the first four amounts");

        // Keep test running so you can see transactions in the listener
        while (true) {
            Thread.sleep(20000);
            logger.info("...");
        }
    }
}
