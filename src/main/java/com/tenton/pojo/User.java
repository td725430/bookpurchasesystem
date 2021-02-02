package com.tenton.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @Date: 2021/1/29
 * @Author: Tenton
 * @Description:
 */
@Entity
@Table(name = "user")
public class User implements Serializable {
    /**
     * 用户Id
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
     * 用户名
     */
    @Column(name = "name")
    private String name;
    /**
     * 密码
     */
    @Column(name = "password")
    private String password;
    /**
     * 姓名
     */
    @Column(name = "full_name")
    private String fullName;
    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;
    /**
     * 电话
     */
    @Column(name = "tel")
    private String tel;
    /**
     * 地址
     */
    @Column(name = "address")
    private String address;
    /**
     * 角色权限
     */
    @Column(name = "role")
    private Integer role;
    /**
     * 密保问题
     */
    @Column(name = "problem")
    private String problem;
    /**
     * 答案
     */
    @Column(name = "answer")
    private String answer;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    public User() {
    }

    /**
     * 注册创建用户时的构造函数
     * @param name
     * @param password
     * @param fullName
     * @param email
     * @param tel
     * @param address
     * @param problem
     * @param answer
     * @param createTime
     * @param updateTime
     */
    public User(String name, String password, String fullName, String email, String tel, String address, String problem, String answer, Timestamp createTime, Timestamp updateTime) {
        this.name = name;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.tel = tel;
        this.address = address;
        this.problem = problem;
        this.answer = answer;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    /**
     * 修改用户信息时所用到的构造函数
     * @param name
     * @param password
     * @param fullName
     * @param email
     * @param tel
     * @param address
     * @param problem
     * @param answer
     * @param updateTime
     */
    public User(String name, String password, String fullName, String email, String tel, String address, String problem, String answer, Timestamp updateTime) {
        this.name = name;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.tel = tel;
        this.address = address;
        this.problem = problem;
        this.answer = answer;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                ", role=" + role +
                ", problem='" + problem + '\'' +
                ", answer='" + answer + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
