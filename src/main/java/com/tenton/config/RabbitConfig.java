package com.tenton.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Date: 2021/2/2
 * @Author: Tenton
 * @Description:
 */
@Configuration
public class RabbitConfig {
    /**
     * 定义延时队列
     * @return
     */
    @Bean("delayQueue")
    public Queue delayQueue(){
        //设置死信交换机和路由key
        return QueueBuilder.durable("delayQueue")
                //如果消息过时，则会被投递到当前对应的my-dlx-exchange
                .withArgument("x-dead-letter-exchange","my-dlx-exchange")
                //如果消息过时，my-dlx-exchange会更具routing-key-delay投递消息到对应的队列
                .withArgument("x-dead-letter-routing-key","routing-key-delay").build();
    }

    /**
     * 定义死信队列
     * @return
     */
    @Bean("dlxQueue")
    public Queue dlxQueue(){
        return QueueBuilder.durable("my-dlx-queue").build();
    }

    /**
     * 定义死信交换机
     * @return
     */
    @Bean("dlxExchange")
    public Exchange dlxExchange(){
        return ExchangeBuilder.directExchange("my-dlx-exchange").build();
    }

    /**
     * 绑定死信队列与交换机
     * @param exchange
     * @param queue
     * @return
     */
    @Bean("dlxBinding")
    public Binding dlxBinding(@Qualifier("dlxExchange") Exchange exchange, @Qualifier("dlxQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("routing-key-delay").noargs();
    }
}
