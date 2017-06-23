package com.xxyh.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by zhugc on 2017/6/22.
 */
public class Send {

    // 设置队列名称
    private static final String queueName = "xxyh_test";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置RabbitMQ服务主机ip或主机名
        factory.setHost("localhost");
        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建一个频道
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(queueName, false, false, false, null);
        System.out.println("准备发送消息................................................................");

        String message = "hello rabbitmq";
        // 发送消息到队列
        channel.basicPublish("", queueName, null, message.getBytes());
        System.out.println(Thread.currentThread().getName() + "发送消息：" + message);

        // 关闭资源
        channel.close();
        connection.close();
    }
}
