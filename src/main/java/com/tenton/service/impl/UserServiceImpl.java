package com.tenton.service.impl;

import com.tenton.dao.UserDao;
import com.tenton.pojo.User;
import com.tenton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Random;

/**
 * @Date: 2021/1/29
 * @Author: Tenton
 * @Description: 用户
 */
@Controller
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    private JavaMailSender mailSender;
    /**
     * 添加用户
     * @param user
     */
    @Override
    public void insertUser(User user) {
        userDao.save(user);
    }
    /**
     * 删除用户
     * @param id
     */
    @Override
    public void deleteUser(int id) {
        userDao.deleteById(id);
    }
    /**
     * 修改用户
     * @param user
     */
    @Override
    public void updateUser(User user) {
        userDao.save(user);
    }
    /**
     * 获取所有用户信息
     * @return
     */
    @Override
    public List<User> listUser() {
        return userDao.findAll();
    }
    /**
     * 查询用户信息
     * @param id
     * @return
     */
    @Override
    public User getUser(int id) {
        return userDao.getOne(id);
    }

    @Override
    public void sendEmail(String email) {
        //创建简单邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        //谁发的 923004696@qq.com
        message.setFrom("923004696@qq.com");
        //谁要接收 这里面可以写数组，可以一次发送
        message.setTo(email);
        //邮件标题-自定义
        message.setSubject("hello");
        //用于保存验证码
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        //随机生成四位数字验证码
        for (int i = 0; i < 4; i++) {
            str.append(random.nextInt(10));
        }

        //邮件内容-自定义
        message.setText("这是你的登录验证码：" + str);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }
}
