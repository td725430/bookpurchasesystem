package com.tenton.controller;

import com.tenton.pojo.User;
import com.tenton.service.impl.UserServiceImpl;
import com.tenton.utils.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Date: 2021/1/29
 * @Author: Tenton
 * @Description:
 */
@Controller
public class LoginController {
    @Autowired
    UserServiceImpl userServiceImpl;
    @RequestMapping("/login")
    public String login(@RequestParam("name") String name,
                        @RequestParam("password") String password,
                        @RequestParam("userType") String userType,
                        Model model, HttpSession session) {
        Integer role = null;
        boolean adminFlag = false;
        if ("管理员".equals(userType)) {
            role = ConstantUtil.ADMIN_TYPE;
            adminFlag = true;
        }else{
            role = ConstantUtil.USER_TYPE;
        }
        List<User> users = userServiceImpl.listUser();
        //判断用户名和密码是否正确
        boolean flag = false;
        A:for (User user : users) {
            if (user.getName().equals(name) && user.getPassword().equals(password) && user.getRole().equals(role)) {
                session.setAttribute("loginUser",name);
                session.setAttribute("userId",user.getId());
                flag = true;
                break A;
            }
        }
        if (flag) {
            if (adminFlag) {
                return "redirect:/admin/listBook";
            }else {
                return "redirect:/user/listBook";
            }
        } else {
            //告诉用户，你登陆失败了
            model.addAttribute("msg", "用户名或密码错误！");
            return "/index";
        }
    }
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "/index";
    }
}