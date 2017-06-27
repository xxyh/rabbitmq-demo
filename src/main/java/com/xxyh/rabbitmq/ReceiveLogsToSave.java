package com.xxyh.rabbitmq;

import com.rabbitmq.client.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * Created by zhugc on 2017/6/27.
 */
public class ReceiveLogsToSave {
    private static final String EXCHANGE_NAME = "test_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, EXCHANGE_NAME, "");

        System.out.println("准备接收消息......");

        final Consumer consumer= new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "utf-8");

                print2File(message);
            }

        };
        channel.basicConsume(queue, true, consumer);
    }

    private static void print2File(String message) throws FileNotFoundException {
        try {
            String dir = ReceiveLogsToSave.class.getClassLoader().getResource("").getPath();
            String logFileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            File file = new File(dir, logFileName + ".txt");
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.flush();
            fos.write((message + "\r\n").getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
