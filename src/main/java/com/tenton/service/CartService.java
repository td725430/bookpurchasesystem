package com.tenton.service;

import com.tenton.pojo.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Date: 2021/1/29
 * @Author: Tenton
 * @Description: 购物车
 */
public interface CartService {
    /**
     * 往购物车中增加数据
     * @param cart
     */
    void insertCart(Cart cart);

    /**
     * 根据Id删除购物车中对应商品
     * @param id
     */
    void deleteCart(int id);

    /**
     * 根据用户删除购物车中对应商品
     * @param userId
     */
    void deleteByUserId(int userId);
    /**
     * 修改购物车中商品
     * @param cart
     */
    void updateCart(Cart cart);

    /**
     * 获取购物车中所有数据
     * @return
     */
    List<Cart> listCart();

    /**
     * 根据用户Id查询购物车中数据
     * @param userId
     * @return
     */
    List<Cart>  getCart(int userId);
    /**
     * 分页查询
     * @param pageable
     * @return
     */
    Page<Cart> findAll(Pageable pageable);

    /**
     * 模糊查询
     * @param name
     * @param pageNum
     * @return
     */
    Page<Cart> pageQuery(String name, Integer pageNum);

    /**
     * 发送延迟队列
     * @param userId
     * @param bookId
     */
    void sendDelayQueue(Integer userId, int bookId);

    /**
     * 根据用户Id和图书Id查询购物车
     * @param userId
     * @param bookId
     * @return
     */
    Cart getCartByUserAndBookId(Integer userId, int bookId);
}
