package com.xxyh.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * Created by zhugc on 2017/6/29.
 */
public class ReceiveLogsDirect {
    private static final String EXCHANGE_NAME = "direct_logs";
    private static final String[] SEVERITIES = {"info", "warning", "error"};

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String queue = channel.queueDeclare().getQueue();
        String severity = getSeverity();

        channel.queueBind(queue, EXCHANGE_NAME, severity);

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "utf-8");
                System.out.println(Thread.currentThread().getName()+" 接收消息: " + message);
            }
        };
        channel.basicConsume(queue, true, consumer);

    }

    // 随机产生一种消息类型
    private static String getSeverity() {
        Random random = new Random();
        int i = random.nextInt(3);
        return SEVERITIES[i];
    }

}
