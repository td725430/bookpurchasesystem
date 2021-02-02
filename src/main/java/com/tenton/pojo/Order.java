package com.tenton.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Date: 2021/2/2
 * @Author: Tenton
 * @Description:
 */
@Entity
@Table(name = "order")
public class Order implements Serializable {
    /**
     * 订单Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" ,updatable = false)
    private Integer id;
    /**
     * 用户Id
     */
    @Column(name = "user_id")
    private Integer userId;
    /**
     * 图书名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 图书售价
     */
    @Column(name = "price")
    private BigDecimal price;
    /**
     * 购买数量
     */
    @Column(name = "num")
    private Integer num;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Timestamp createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Order() {
    }

    public Order(Integer userId, String name, BigDecimal price, Integer num, Timestamp createTime) {
        this.userId = userId;
        this.name = name;
        this.price = price;
        this.num = num;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", num=" + num +
                ", createTime=" + createTime +
                '}';
    }
}
