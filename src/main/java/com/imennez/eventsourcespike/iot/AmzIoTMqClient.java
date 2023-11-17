package com.imennez.eventsourcespike.iot;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class AmzIoTMqClient {

    private Channel channel;
    private String queueName;

    public AmzIoTMqClient(String uri, String username, String password) throws AmzIotInitException {

        try {
            // Recreate all because credentials might change at any time in the Admin console
            Connection connection = getConnection(uri, username, password);
            this.channel = connection.createChannel();
            this.queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, "iot-messages", "");
            System.out.println("Connection to AmazonMQ established");
        } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException | IOException | TimeoutException e) {
            throw new AmzIotInitException("Failed to initialize Amazon IoT MQ Client", e);
        }
    }

    public void bind(Consumer<String> callback) throws AmzIotInitException {
        try {
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Received Message: " + message);
                callback.accept(message);
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        } catch (IOException e) {
            throw new AmzIotInitException("Could not bind callback method to client", e);
        }
    }

    public void close() {
        try {
            this.channel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection getConnection(String uri, String username, String password)
            throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException, IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri);
        factory.setPort(5671); // Default port for AmazonMQ with SSL
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost("/");
        factory.setAutomaticRecoveryEnabled(false);

        return factory.newConnection();
    }
}
