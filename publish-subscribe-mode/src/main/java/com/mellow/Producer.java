package com.mellow;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhengxinyu
 * @date 2023/7/2
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
        String exchangeName = "PS-fanout-exchange";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, true, false, false,null);
        String queueOne = "queue-one";
        String queueTwo = "queue-two";
        channel.queueDeclare(queueOne, true,false, false,null);
        channel.queueDeclare(queueTwo, true,false, false,null);
        channel.queueBind(queueOne, exchangeName, "");
        channel.queueBind(queueTwo, exchangeName, "");
        String message01 = "我是兔子，";
        String message02 = "你好吗？";
        channel.basicPublish(exchangeName, "", null, message01.getBytes());
        channel.basicPublish(exchangeName, "", null, message02.getBytes());
        channel.close();
        connection.close();
    }
}
