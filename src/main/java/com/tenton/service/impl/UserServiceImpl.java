package com.tenton.service.impl;

import com.tenton.dao.UserDao;
import com.tenton.pojo.User;
import com.tenton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Date: 2021/1/29
 * @Author: Tenton
 * @Description: 用户
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    RedisTemplate redisTemplate;
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
        //邮件内容-自定义
        Random random = new Random();
        String str = random.nextInt(10000) + "";
        int randLength = str.length();
        //生成4位数验证码
        if(randLength<4){
            for(int i=1; i<=4-randLength; i++){
                str = "0"+ str ;
            }
        }
        int num = Integer.parseInt(str.trim());
        redisTemplate.opsForValue().set(email,num);
        //邮件内容-自定义
        message.setText("这是你的登录验证码：" + str);
        //设置注册验证码300秒的过期时间
        redisTemplate.expire(email,300, TimeUnit.SECONDS);
        mailSender.send(message);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }
}
