package com.tenton.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Date: 2021/1/29
 * @Author: Tenton
 * @Description:
 */
@Entity
@Table(name = "book")
public class Book implements Serializable {
    /**
     * 图书Id
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
     * 图书名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 作者
     */
    @Column(name = "author")
    private String author;
    /**
     * 价格
     */
    @Column(name = "price")
    private BigDecimal price;
    /**
     * 库存
     */
    @Column(name = "num")
    private Integer num;
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Book() {
    }

    public Book(String name, String author, BigDecimal price, Integer num, Timestamp createTime, Timestamp updateTime) {
        this.name = name;
        this.author = author;
        this.price = price;
        this.num = num;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Book(String name, String author, BigDecimal price, Integer num, Timestamp updateTime) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.price = price;
        this.num = num;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", num=" + num +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}


