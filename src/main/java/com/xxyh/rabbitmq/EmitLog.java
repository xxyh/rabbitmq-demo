package com.xxyh.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * Created by zhugc on 2017/6/27.
 */
public class EmitLog {
    private static final String EXCHANGE_NAME = "test_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String message = Thread.currentThread().getName() + " -- " + format.format(new Date()) + " logging...";
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
        System.out.println(Thread.currentThread().getName() + " 发送消息：" + message);

        channel.close();
        connection.close();

    }
}
