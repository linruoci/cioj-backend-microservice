package com.ruoci.ciojbackendjudgeservice.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;


/**
 * @Author: ruoci
 **/
@Slf4j
public class InitRabbitMq {


    public static void doInit(){
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();

            String EXCHANGE_NAME = "code_exchange";
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

//            创建队列
            String queueName = "code_queue";
            channel.queueDeclare(queueName, true, false, false, null);

            channel.queueBind(queueName, EXCHANGE_NAME, "my_routingKey");

        } catch (Exception e) {
            log.error("启动失败!");
        }
    }

    public static void main(String[] args) {


        doInit();

    }


}
