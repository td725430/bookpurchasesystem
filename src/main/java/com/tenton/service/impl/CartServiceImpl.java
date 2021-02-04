package com.tenton.service.impl;

import com.mysql.cj.util.StringUtils;
import com.tenton.dao.CartDao;
import com.tenton.pojo.Cart;
import com.tenton.service.CartService;
import com.tenton.utils.ConstantUtil;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date: 2021/1/29
 * @Author: Tenton
 * @Description:
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    CartDao cartDao;
    @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     * 往购物车中增加数据
     * @param cart
     */
    @Override
    public void insertCart(Cart cart) {
        cartDao.save(cart);
    }

    /**
     * 根据Id删除购物车中对应商品
     * @param id
     */
    @Override
    public void deleteCart(int id) {
        cartDao.deleteById(id);
    }

    /**
     * 根据用户删除购物车中对应商品
     * @param userId
     */
    @Override
    public void deleteByUserId(int userId) {
        cartDao.deleteAllByUserId(userId);
    }

    /**
     * 修改购物车中商品
     * @param cart
     */
    @Override
    public void updateCart(Cart cart) {
        cartDao.save(cart);
    }

    /**
     * 获取购物车中所有数据
     * @return
     */
    @Override
    public List<Cart> listCart() {
        return cartDao.findAll();
    }

    /**
     * 根据用户Id查询购物车中数据
     * @param userId
     * @return
     */
    @Override
    public List<Cart> getCart(int userId) {
        return cartDao.findAllByUserId(userId);
    }

    /**
     * 分页查询
     * @param pageable
     * @return
     */
    @Override
    public Page<Cart> findAll(Pageable pageable) {
        return cartDao.findAll(pageable);
    }
    /**
     * 模糊查询
     * @param name
     * @param pageNum
     * @return
     */
    @Override
    public Page<Cart> pageQuery(String name, Integer pageNum) {
        Specification<Cart> specification = new Specification<Cart>() {
            @Override
            public Predicate toPredicate(Root<Cart> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = null;
                //判断你是否输入了查询条件，如果有条件，就通过接口拼装sql进行模糊查询
                if (!StringUtils.isNullOrEmpty(name)) {
                    predicate = cb.like(root.get("name").as(String.class), "%" + name + "%");
                }
                return predicate;

            }
        };
        Pageable pageable = PageRequest.of(pageNum-1, ConstantUtil.PAGE_RECORD_NUM);
        Page<Cart> list = cartDao.findAll(specification, pageable);
        return list;
    }
    /**
     * 发送延迟队列
     * @param userId
     * @param bookId
     */
    @Override
    public void sendDelayQueue(Integer userId, int bookId) {
        Cart cart = cartDao.findCartByUserIdAndBookId(userId, bookId);
        Map<String,Cart> map = new HashMap<>();
        //将cart信息发送到rabbitMQ中
        map.put("msg",cart);
        rabbitTemplate.convertAndSend("delayQueue", map, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置30分钟自动过期，删除cart信息
                message.getMessageProperties().setExpiration("1800000");
                return message;
            }
        });
    }

    /**
     * 根据用户Id和图书Id查询购物车
     * @param userId
     * @param bookId
     * @return
     */
    @Override
    public Cart getCartByUserAndBookId(Integer userId, int bookId) {
        return cartDao.findCartByUserIdAndBookId(userId,bookId);
    }

}
