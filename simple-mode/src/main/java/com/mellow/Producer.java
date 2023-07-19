package com.mellow;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author zhengxinyu
 * @date 2023/7/1
 */
public class Producer {

    public static void main(String[] args) throws Exception {
        // 获取连接工厂
        ConnectionFactory connectionFactory = Producer.getConnectionFactory();
        // 通过工厂获取连接
        Connection connection = Producer.getConnection(connectionFactory);
        // 通过连接获取通道
        Channel channel = Producer.createQueue(connection);
        // 声明队列
        channel.queueDeclare("simple_queue", true, false, false, null);
        // 发送消息
        String message = "你好，兔子";
        channel.basicPublish("", "simple_queue", null, message.getBytes());
        // 关闭连接
        channel.close();
        connection.close();
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
