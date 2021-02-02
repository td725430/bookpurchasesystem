package com.tenton.service.impl;

import com.mysql.cj.util.StringUtils;
import com.tenton.dao.OrderDao;
import com.tenton.pojo.Order;
import com.tenton.service.OrderService;
import com.tenton.utils.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @Date: 2021/2/2
 * @Author: Tenton
 * @Description:
 */
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDao orderDao;
    /**
     * 创建订单
     * @param order
     */
    @Override
    public void insertOrder(Order order) {
        orderDao.save(order);
    }
    /**
     * 查询所有订单
     * @return
     */
    @Override
    public List<Order> listOrder() {
        return orderDao.findAll();
    }
    /**
     * 根据用户Id查询订单
     * @param userId
     * @return
     */
    @Override
    public Order getOrder(int userId) {
        return orderDao.getOne(userId);
    }
    /**
     * 分页查询
     * @param pageable
     * @return
     */
    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderDao.findAll(pageable);
    }

    /**
     * 模糊查询
     * @param name
     * @param pageNum
     * @return
     */
    @Override
    public Page<Order> pageQuery(String name, Integer pageNum) {
        Specification<Order> specification = new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = null;
                //判断你是否输入了查询条件，如果有条件，就通过接口拼装sql进行模糊查询
                if (!StringUtils.isNullOrEmpty(name)) {
                    predicate = cb.like(root.get("name").as(String.class), "%" + name + "%");
                }
                return predicate;
            }
        };
        Pageable pageable = PageRequest.of(pageNum-1, ConstantUtil.PAGE_RECORD_NUM);
        Page<Order> list = orderDao.findAll(specification, pageable);
        return list;
    }
}
