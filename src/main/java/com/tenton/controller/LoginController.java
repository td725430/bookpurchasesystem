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
 * @Description: 登录 退出系统
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
        //角色权限
        Integer role = null;
        //是否为管理员
        boolean adminFlag = false;
        //前端选择管理员，角色赋予管理员权限
        if ("管理员".equals(userType)) {
            role = ConstantUtil.ADMIN_TYPE;
            adminFlag = true;
        }else{
            //否则赋予用户权限
            //由于前端默认选择的是用户，所有不存在null值
            role = ConstantUtil.USER_TYPE;
        }
        //查询所有用户信息
        List<User> users = userServiceImpl.listUser();
        //判断用户名和密码和权限是否正确
        boolean flag = false;
        A:for (User user : users) {
            //用户名、密码、角色权限全都正确
            if (user.getName().equals(name) && user.getPassword().equals(password) && user.getRole().equals(role)) {
                //往session域中存储登录用户的用户名，用于前端获取实现欢迎用户的显示
                session.setAttribute("loginUser",name);
                //往session域中存储登录用户的用户Id
                session.setAttribute("userId",user.getId());
                flag = true;
                break A;
            }
        }
        if (flag) {
            //权限为管理员，跳转到管理员的查询所有图书方法
            if (adminFlag) {
                return "redirect:/admin/listBook";
            }else {
                //权限为用户，跳转到用户的查询所有图书方法
                return "redirect:/user/listBook";
            }
        } else {
            //将错误信息存储到model中，传递给前端，告诉用户，你登陆失败了
            model.addAttribute("msg", "用户名或密码错误！");
            return "/index";
        }
    }

    /**
     * 退出
     * @param session
     * @return
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        //是session无效化，实现拦截
        session.invalidate();
        return "/index";
    }
}