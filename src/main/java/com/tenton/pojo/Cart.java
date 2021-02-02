package com.tenton.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @Date: 2021/1/29
 * @Author: Tenton
 * @Description:
 */
@Entity
@Table(name = "cart")
public class Cart implements Serializable {
    /**
     * 购物车Id
     * @Id：声明主键的配置
     * @GeneratedValue:配置主键的生成策略 自增
     * @Column：配置属性和字段的映射关系
     *      name：数据库中字段的名称
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
     * 图书Id
     */
    @Column(name = "book_id")
    private Integer bookId;
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
     * 作者
     */
    @Column(name = "author")
    private String author;
    /**
     * 图书库存
     */
    @Column(name = "stock")
    private Integer stock;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Timestamp createTime;
    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Timestamp updateTime;

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

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Cart() {
    }

    public Cart(Integer userId, Integer bookId, String name, BigDecimal price, Integer num, String author, Integer stock, Timestamp createTime, Timestamp updateTime) {
        this.userId = userId;
        this.bookId = bookId;
        this.name = name;
        this.price = price;
        this.num = num;
        this.author = author;
        this.stock = stock;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Cart(Integer userId, Integer bookId, String name, BigDecimal price, Integer num, String author, Integer stock, Timestamp updateTime) {
        this.userId = userId;
        this.bookId = bookId;
        this.name = name;
        this.price = price;
        this.num = num;
        this.author = author;
        this.stock = stock;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", userId=" + userId +
                ", bookId=" + bookId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", num=" + num +
                ", author='" + author + '\'' +
                ", stock=" + stock +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
