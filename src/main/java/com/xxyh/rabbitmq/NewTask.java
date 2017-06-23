package com.xxyh.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by zhugc on 2017/6/22.
 */
public class NewTask {
    private static final String queueName = "task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 持久化
        boolean durable = true;
        channel.queueDeclare(queueName, durable, false, false, null);

        // 发送10条记录，每次在后面添加"."
        for (int i = 0; i < 10; i++) {
            String dot = "";
            for (int j = 0; j <= i; j++) {
                dot += ".";
            }
            String message = "work queue " + dot + dot.length();
            channel.basicPublish("", queueName, null, message.getBytes("utf-8"));
            System.out.println(Thread.currentThread().getName() + "发送消息：" + message);
        }
        channel.close();
        connection.close();
    }
}
