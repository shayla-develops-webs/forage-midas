package com.jpmc.midascore;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class TaskThreeTests {
    static final Logger logger = LoggerFactory.getLogger(TaskThreeTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;
@Autowired
private UserRepository userRepository;

    @Test
void task_three_verifier() throws InterruptedException {
    userPopulator.populate();
    String[] transactionLines = fileLoader.loadStrings("/test_data/mnbvcxz.vbnm");

    // send transactions
    for (String transactionLine : transactionLines) {
        kafkaProducer.send(transactionLine);
    }

    // wait a bit to make sure transactions are processed
    Thread.sleep(2000);

    // BEGIN: simple loop to find users
    UserRecord waldorf = null;
    for (UserRecord user : userRepository.findAll()) {
        if (user.getName().equalsIgnoreCase("waldorf")) {
            waldorf = user;
            break; // stop once we find Waldorf
        }
    }

    if (waldorf != null) {
        logger.info("Waldorf's final balance: " + waldorf.getBalance());
    } else {
        logger.info("Waldorf not found!");
    }
    // END

    logger.info("----------------------------------------------------------");
    logger.info("use your debugger to find out other users' balances if needed");

    // Keep the test running so you can inspect in debugger
    while (true) {
        Thread.sleep(20000);
        logger.info("...");
    }
}
}