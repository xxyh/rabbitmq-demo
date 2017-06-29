package com.xxyh.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by zhugc on 2017/6/29.
 */
public class EmitLogsTopic {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 声明 topic类型的交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String[] routingKeys = new String[]{"zhang.info", "li.warning", "wang.info", "zhang.error"};

        for (String routingKey : routingKeys) {
            String message = UUID.randomUUID().toString();
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("utf-8"));

            System.out.println(Thread.currentThread().getName() + " 发送消息： " + message);
        }

        channel.close();
        connection.close();
    }
}
