package com.xxyh.rabbitmq;

import com.rabbitmq.client.*;
import sun.java2d.loops.TransformHelper;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by zhugc on 2017/6/29.
 */
public class ReceiveLogsTopic {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queue = channel.queueDeclare().getQueue();

        // 接收所有zhang发出的消息
        channel.queueBind(queue, EXCHANGE_NAME, "zhang.*");

        System.out.println("准备接收所有zhang发出的消息----------------");

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "utf-8");
                System.out.println(Thread.currentThread().getName() + " 接收到消息： " + message);
            }
        };

        channel.basicConsume(queue, true, consumer);

    }
}
