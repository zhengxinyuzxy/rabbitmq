package com.mellow;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhengxinyu
 * @date 2023/7/1
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.200.200");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
//        channel.queueDeclare("work_queue", true, false, false, null);
        for (int i = 10; i < 20; i++) {
            String message = "兔子"+ i + "号，来报道";
            channel.basicPublish("", "work_queue", null, message.getBytes());
        }
        channel.close();
        connection.close();
    }
}
