package com.mellow;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author zhengxinyu
 * @date 2023/7/1
 */
public class Consumer {
    public static void main(String[] args) throws IOException {
        ConnectionFactory connectionFactory = Consumer.getConnectionFactory();
        Connection connection = Consumer.getConnection(connectionFactory);
        Channel channel = Consumer.createQueue(connection);
        // 创建队列Queue
//        channel.queueDeclare("simple_queue", true, false, false, null);
        // 回调对象
        DefaultConsumer defaultConsumer = Consumer.getDefaultConsumer(channel);
        // 三个参数
        // 1.queue：队列名称
        // 2.autoAck：是否自动确认
        // 3.callback：回调对象
        channel.basicConsume("simple_queue",true, defaultConsumer);
//        channel.close();
//        connection.close();

    }

    public static DefaultConsumer getDefaultConsumer(Channel channel) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
//                super.handleDelivery(consumerTag, envelope, properties, body);
                System.out.println(consumerTag);
                System.out.println(envelope.getExchange());
                System.out.println(envelope.getRoutingKey());
                System.out.println(properties);
                System.out.println(new String(body));
            }
        };
    }

    public static Channel createQueue(Connection connection) {
        Channel channel = null;
        try {
            channel = connection.createChannel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return channel;
    }

    public static Connection getConnection(ConnectionFactory connectionFactory) {
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static ConnectionFactory getConnectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.200.200");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }
}
