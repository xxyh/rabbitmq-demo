package com.xxyh.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by zhugc on 2017/6/29.
 */
public class EmitLogDirect {
    private static final String EXCHANGE_NAME = "direct_logs";
    private static final String[] SEVERITIES = {"info", "warning", "error"};

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 发送10条消息
        for (int i = 0; i < 10; i++) {
            String severity = getSeverity();
            String message = severity + "... log ..." + UUID.randomUUID().toString();
            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes("utf-8"));

            System.out.println(Thread.currentThread().getName()+" 发送消息：" + message);
        }

        channel.close();
        connection.close();
    }

    // 随机产生一种消息类型
    private static String getSeverity() {
        Random random = new Random();
        int i = random.nextInt(3);
        return SEVERITIES[i];
    }

}
