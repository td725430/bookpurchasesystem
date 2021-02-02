package com.tenton.controller;

import com.tenton.pojo.Book;
import com.tenton.pojo.Cart;
import com.tenton.pojo.User;
import com.tenton.service.impl.BookServiceImpl;
import com.tenton.service.impl.CartServiceImpl;
import com.tenton.service.impl.UserServiceImpl;
import com.tenton.utils.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Date: 2021/1/30
 * @Author: Tenton
 * @Description:
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
    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping(value = "/regist")
    public String regist(User  user){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        user.setRole(ConstantUtil.USER_TYPE);
        user.setCreateTime(timestamp);
        user.setUpdateTime(timestamp);
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
    }
    /**
     * 获取所有图书信息
     * @param model
     * @return
     */
    @RequestMapping("/listBook")
    public String listBook(Model model, HttpServletResponse response, Integer pageNum) {
        if (pageNum == null){
            pageNum = 1;
        }
        // （当前页， 每页记录数）
        Pageable pageable = PageRequest.of(pageNum - 1, ConstantUtil.PAGE_RECORD_NUM);
        Page<Book> list = bookServiceImpl.findAll(pageable);
        model.addAttribute("books", list);
        // 允许iframe
        response.addHeader("x-frame-options","SAMEORIGIN");
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
        if (pageNum == null){
            pageNum = 1;
        }
        Page<Book> list = bookServiceImpl.pageQuery(name, pageNum);
        for (Book book : list) {
            System.out.println(book.toString());
        }
        model.addAttribute("bookList", list);
        // 允许iframe
        response.addHeader("x-frame-options","SAMEORIGIN");
        return "user/getBook";
    }

    /**
     * 添加商品到购物车中
     * @param id bookId
     * @param session
     * @param num
     * @return
     */
    /**
     * 从session域中获取登录用户Id
     * 获取当前时间，并将时间赋值于cart对象的创建时间和修改时间
     */
    @PostMapping("/addCart")
    public String addCart(Cart cart,HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        Book book = bookServiceImpl.getBook(cart.getBookId());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Cart cart1 = new Cart(userId,book.getId(),book.getName(),book.getPrice(),cart.getNum(),book.getAuthor(),book.getNum(),timestamp,timestamp);
        cartServiceImpl.insertCart(cart1);
        return "redirect:/user/listCart";
    }

    /**
     * 获取所有图书信息
     * @param model
     * @return
     */
    @RequestMapping("/listCart")
    public String listCart(Model model, HttpServletResponse response, Integer pageNum) {
        if (pageNum == null){
            pageNum = 1;
        }
        Book book = null;
                // （当前页，每页记录数）
        Pageable pageable = PageRequest.of(pageNum - 1, ConstantUtil.PAGE_RECORD_NUM);
        Page<Cart> list = cartServiceImpl.findAll(pageable);
        for (Cart cart : list) {
            book = bookServiceImpl.getBook(cart.getId());
            cart.setStock(book.getNum());
        }
        model.addAttribute("carts", list);
        // 允许iframe
        response.addHeader("x-frame-options","SAMEORIGIN");
        return "user/listCart";
    }

    /**
     * 个人中心
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/getUser")
    public String getUser(Model model,HttpSession session){
        int userId = (int) session.getAttribute("userId");
        User user = userServiceImpl.getUser(userId);
        model.addAttribute("user",user);
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
        int userId = (int) session.getAttribute("userId");
        User user = userServiceImpl.getUser(userId);
        model.addAttribute("user",user);
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
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        int userId = (int) session.getAttribute("userId");
        User oldUser = userServiceImpl.getUser(userId);
        user.setId(userId);
        user.setRole(ConstantUtil.USER_TYPE);
        user.setCreateTime(oldUser.getCreateTime());
        user.setUpdateTime(timestamp);
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
     * 根据图书Id删除图书
     * @param id
     * @return
     */
    @GetMapping("/deleteCart/{id}")
    public String deleteCart(@PathVariable("id")Integer id){
        cartServiceImpl.deleteCart(id);
        return "redirect:/user/listCart";
    }
    /**
     * 根据图书Id删除图书
     * @param session
     * @return
     */
    @GetMapping("/deleteAllCart")
    public String deleteAllCart(HttpSession session){
        int userId = (int) session.getAttribute("userId");
        cartServiceImpl.deleteByUserId(userId);
        return "redirect:/user/listCart";
    }
    @GetMapping("/addOrder")
    public String addOrder(HttpSession session){
        int userId = (int) session.getAttribute("userId");
        List<Cart> cart = cartServiceImpl.getCart(userId);
        return null;
    }
    @GetMapping("/sendEmail/{email}")
    public void sendEmail(@PathVariable("email") String email){
        userServiceImpl.sendEmail(email);
    }
}
