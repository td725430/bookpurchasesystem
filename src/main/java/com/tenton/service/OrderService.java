package com.tenton.service;

import com.tenton.pojo.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Date: 2021/2/2
 * @Author: Tenton
 * @Description:
 */
public interface OrderService {
    /**
     * 创建订单
     * @param order
     */
    void insertOrder(Order order);

    /**
     * 查询所有订单
     * @return
     */
    List<Order> listOrder();
    /**
     * 根据用户Id查询订单
     * @param userId
     * @return
     */
    Order getOrder(int userId);
    /**
     * 分页查询
     * @param pageable
     * @return
     */
    Page<Order> findAll(Pageable pageable);

    /**
     * 模糊查询
     * @param name
     * @param pageNum
     * @return
     */
    Page<Order> pageQuery(String name, Integer pageNum);
}
