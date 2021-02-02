package com.tenton.service;

import com.tenton.pojo.User;

import java.util.List;

/**
 * @Date: 2021/1/29
 * @Author: Tenton
 * @Description: 用户
 */
public interface UserService {
    /**
     * 添加用户
     * @param user
     */
    void insertUser(User user);

    /**
     * 删除用户
     * @param id
     */
    void deleteUser(int id);

    /**
     * 修改用户
     * @param user
     */
    void updateUser(User user);

    /**
     * 获取所有用户信息
     * @return
     */
    List<User> listUser();

    /**
     * 查询用户信息
     * @param id
     * @return
     */
    User getUser(int id);

    /**
     * 发送注册邮件
     */
    void sendEmail(String email);
}
