package com.ruoci.ciojbackendjudgeservice.rabbitmq;

import com.rabbitmq.client.Channel;
import com.ruoci.ciojbackendjudgeservice.judge.JudgeService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author: ruoci
 **/
@Component
@Slf4j
public class MyMessageConsumer {


    @Resource
    private JudgeService judgeService;

    @SneakyThrows
    @RabbitListener(queues = {"code_queue"}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag){
        log.info("receiveMessage message = {}", message);

        try{
            long questionSubmitId = Long.parseLong(message);
            judgeService.doJudge(questionSubmitId);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e){
            channel.basicNack(deliveryTag, false, false);
        }

    }


}
