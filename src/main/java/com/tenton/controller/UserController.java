package com.tenton.controller;

import com.tenton.pojo.Book;
import com.tenton.pojo.User;
import com.tenton.service.impl.BookServiceImpl;
import com.tenton.service.impl.CartServiceImpl;
import com.tenton.service.impl.UserServiceImpl;
import com.tenton.utils.ConstantUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

/**
 * @Date: 2021/1/30
 * @Author: Tenton
 * @Description: 用户操作
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    BookServiceImpl bookServiceImpl;
    @Autowired
    CartServiceImpl cartServiceImpl;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    /**
     * 用户注册
     * @param user
     * @param inputCode
     * @return
     */
    @PostMapping(value = "/regist")
    public String regist(User user,String inputCode,Model model){
        //获取存储在redis中的注册验证码
        String code = redisTemplate.opsForValue().get(user.getEmail());
        //判断用户输入的验证码和注册码是否一致
        if (code.equals(inputCode)) {
            //删除验证码
            redisTemplate.delete(user.getEmail());
            //获取当前时间
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            //赋予用户角色权限为用户
            user.setRole(ConstantUtil.USER_TYPE);
            //给新建的用户对象赋予权限和创建时间（此时创建时间和修改时间一致）
            user.setCreateTime(timestamp);
            user.setUpdateTime(timestamp);
            //根据前端用户选择的密保问题Id，赋予问题
            if (ConstantUtil.PROBLEM_NUM1.equals(user.getProblem())){
                user.setProblem(ConstantUtil.PROBLEM_CN1);
            }else if (ConstantUtil.PROBLEM_NUM2.equals(user.getProblem())){
                user.setProblem(ConstantUtil.PROBLEM_CN2);
            }else if (ConstantUtil.PROBLEM_NUM3.equals(user.getProblem())){
                user.setProblem(ConstantUtil.PROBLEM_CN3);
            }else if (ConstantUtil.PROBLEM_NUM4.equals(user.getProblem())){
                user.setProblem(ConstantUtil.PROBLEM_CN4);
            }
            userServiceImpl.insertUser(user);
            return "/index";
        }else if (!code.equals(inputCode)){
            //发送一个错误信息到前端，并返回到前端注册界面,告诉用户，你注册失败了
            model.addAttribute("msg", "验证码不正确！");
            return "/regist";
        }
        return null;
    }
    /**
     * 获取所有图书信息
     * @param model
     * @return
     */
    @RequestMapping("/listBook")
    public String listBook(Model model, HttpServletResponse response, Integer pageNum) {
        //判断前端传递过来的pageNum是否为空
        if (pageNum == null){
            pageNum = 1;
        }
        //创建一个Pageable对象用于封装pageNUm和每页显示数据数量
        // （当前页， 每页记录数）
        Pageable pageable = PageRequest.of(pageNum - 1, ConstantUtil.PAGE_RECORD_NUM);
        //根据pageable对象查询当前登录用户的图书信息
        Page<Book> list = bookServiceImpl.findAll(pageable);
        model.addAttribute("books", list);
        // 允许iframe
        response.addHeader("x-frame-options","SAMEORIGIN");
        //跳转到用户模块下的图书界面
        return "user/list";
    }
    /**
     * 跳转到查询图书界面
     * @return
     */
    @GetMapping("/toGetBook")
    public String toGetBook(){
        return "user/getBook";
    }
    /**
     * 根据图书名称模糊查询图书信息，并将对应书籍信息传递给前端界面
     * @param name
     * @param model
     * @return
     */
    @GetMapping("/getBook")
    public String getBook(Model model, HttpServletResponse response,String name,Integer pageNum){
        //判断前端传递过来的pageNum是否为空
        if (pageNum == null){
            pageNum = 1;
        }
        //模糊查询
        Page<Book> list = bookServiceImpl.pageQuery(name, pageNum);
        model.addAttribute("bookList", list);
        // 允许iframe
        response.addHeader("x-frame-options","SAMEORIGIN");
        //跳转到用户模块下的查询图书界面
        return "user/getBook";
    }

    /**
     * 个人中心
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/getUser")
    public String getUser(Model model,HttpSession session){
        //获取session域中的登录用户Id
        int userId = (int) session.getAttribute("userId");
        //根据用户Id查询用户信息
        User user = userServiceImpl.getUser(userId);
        model.addAttribute("user",user);
        //跳转到用户模块下的个人中心
        return "user/getUser";
    }

    /**
     * 携带原始用户信息跳转到修改界面
     * @param model
     * @param session
     * @return
     */
    @PostMapping("/toUpdateUser")
    public String toUpdateUser(Model model,HttpSession session){
        //获取session域中的登录用户Id
        int userId = (int) session.getAttribute("userId");
        //根据用户Id查询用户信息
        User user = userServiceImpl.getUser(userId);
        model.addAttribute("user",user);
        //跳转到用户模块下的修改界面
        return "user/updateUser";
    }

    /**
     * 修改用户信息
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/updateUser")
    public String updateUser(User user,HttpSession session){
        //当前时间
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //从session域中获取登录用户Id
        int userId = (int) session.getAttribute("userId");
        //根据Id查询用户
        User oldUser = userServiceImpl.getUser(userId);
        //给前端传递过来的用户对象赋予查询到的用户对象的id
        user.setId(userId);
        //给前端传递过来的用户对象赋予查询到的用户对象的角色权限
        user.setRole(ConstantUtil.USER_TYPE);
        //给前端传递过来的用户对象赋予查询到的用户对象的创建时间
        user.setCreateTime(oldUser.getCreateTime());
        //给前端传递过来的用户对象赋予查询到的用户对象的修改时间
        user.setUpdateTime(timestamp);
        //判断前端选择的密保Id,赋予密保问题
        if (ConstantUtil.PROBLEM_NUM1.equals(user.getProblem())){
            user.setProblem(ConstantUtil.PROBLEM_CN1);
        }else if (ConstantUtil.PROBLEM_NUM2.equals(user.getProblem())){
            user.setProblem(ConstantUtil.PROBLEM_CN2);
        }else if (ConstantUtil.PROBLEM_NUM3.equals(user.getProblem())){
            user.setProblem(ConstantUtil.PROBLEM_CN3);
        }else if (ConstantUtil.PROBLEM_NUM4.equals(user.getProblem())){
            user.setProblem(ConstantUtil.PROBLEM_CN4);
        }
        userServiceImpl.updateUser(user);
        return "redirect:/user/getUser";
    }

    /**
     * 发送注册邮件
     * @param email
     */
    @GetMapping("/sendEmail")
    public void sendEmail(String email){
        //这里loginEmail是队列名
        rabbitTemplate.convertAndSend("loginEmail",email);
    }
}
