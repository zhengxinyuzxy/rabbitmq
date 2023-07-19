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
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String exchange = "routing-exchange";
        channel.exchangeDeclare(exchange,
                BuiltinExchangeType.DIRECT,
                true,
                false,
                false,
                null);
        String queueOne = "queue-one";
        String queueTwo = "queue-two";
        channel.queueDeclare(queueOne, true,false, false, null);
        channel.queueDeclare(queueTwo, true,false, false, null);
        channel.queueBind(queueOne, exchange, "error");
        channel.queueBind(queueTwo, exchange, "info");
        channel.queueBind(queueTwo, exchange, "warning");
        channel.queueBind(queueTwo, exchange, "error");
        String warningMessage = "warning消息";
        String errorMessage = "error消息";
        channel.basicPublish(exchange, "warning", null, warningMessage.getBytes());
        channel.basicPublish(exchange, "error", null, errorMessage.getBytes());
        channel.close();
        connection.close();
    }
}
