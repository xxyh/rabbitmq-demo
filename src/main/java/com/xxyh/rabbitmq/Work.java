package com.xxyh.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by zhugc on 2017/6/22.
 */
public class Work {

    private static final String queueName = "task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        boolean durable = true;
        channel.queueDeclare(queueName, durable, false, false, null);

        // 设置同一个消费者在同一时间只能消费一条消息
        channel.basicQos(1);

        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(Thread.currentThread().getName() + "接收消息：" + message);
                try {
                    doWork(message);
                    System.out.println("消息接收完毕");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    // 确认消息已经收到
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }


        };

        // 取消autoAck
        boolean autoAck = false;
        channel.basicConsume(queueName, autoAck, consumer);
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
        }
    }
}
