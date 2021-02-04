package com.tenton.listener;

import com.tenton.pojo.Cart;
import com.tenton.service.CartService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * @Date: 2021/2/2
 * @Author: Tenton
 * @Description:
 */
@Component
@RabbitListener(queues = "my-dlx-queue")
public class CartListener {
    @Autowired
    CartService cartService;
    @RabbitHandler
    public void receive(HashMap<String, Cart> map){
        //判断cart是否修改
        boolean flag = false;
        //用户Id是否一致
        boolean userFlag = false;
        int userId = 0;
        //cart是rabbitMQ里面是购物车数据cart
        A:for (Cart cart : map.values()){
            //根据用户Id查询对应的cart信息
            List<Cart> carlist = cartService.getCart(cart.getUserId());
            for (Cart car1 : carlist) {
                //根据用户Id和cart修改时间是否与之前一致，
                //用户Id一致,修改时间不一致，则说明cart信息有修改，不删除cart信息
                //判断用户Id是否一致
                if (cart.getUserId().equals(car1.getUserId())) {
                    userFlag = true;
                    userId = cart.getUserId();
                    //判断修改时间是否不一致
                    if (!cart.getUpdateTime().equals(car1.getUpdateTime())) {
                        //购物车信息修改
                        flag = true;
                        //跳出所有循环
                        break A;
                    }
                }
            }
            //cart信息未修改，但用户Id一致，则删除对应所有cart信息
            if (flag == false && userFlag == true){
                cartService.deleteByUserId(userId);
            }
        }
    }
}
