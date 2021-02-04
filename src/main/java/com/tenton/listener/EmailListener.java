package com.tenton.listener;

import com.tenton.service.UserService;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Date: 2021/2/2
 * @Author: Tenton
 * @Description: 发送注册邮件
 */
@Component
@RabbitListener(queuesToDeclare = @Queue("loginEmail"))
public class EmailListener {
    @Autowired
    UserService userService;
    @RabbitHandler
    public void sendEmail(String email){
        userService.sendEmail(email);
    }
}
