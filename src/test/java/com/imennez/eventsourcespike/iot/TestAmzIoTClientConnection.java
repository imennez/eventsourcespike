package com.imennez.eventsourcespike.iot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestAmzIoTClientConnection {
    @Test
    void test_connect() throws AmzIotInitException {
        AmzIoTMqClient client = new AmzIoTMqClient(
                "amqps://<uri>",
                "<user>>",
                "<password>");

        client.bind((msg) -> {
            System.out.println("Message from MQ: " + msg);
        });
    }

}
