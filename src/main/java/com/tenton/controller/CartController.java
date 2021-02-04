package com.tenton.controller;

import com.tenton.pojo.Book;
import com.tenton.pojo.Cart;
import com.tenton.service.impl.BookServiceImpl;
import com.tenton.service.impl.CartServiceImpl;
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
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Date: 2021/2/2
 * @Author: Tenton
 * @Description: 用户购物车管理
 */
@Controller
@RequestMapping("/user")
public class CartController {
    @Autowired
    BookServiceImpl bookServiceImpl;
    @Autowired
    CartServiceImpl cartServiceImpl;
    /**
     * 添加商品到购物车中
     * @param cart
     * @param session
     * @return
     */
    @PostMapping("/addCart")
    public String addCart(Cart cart, HttpSession session){
        //从session域中获取登录用户Id
        Integer userId = (Integer) session.getAttribute("userId");
        Book book = bookServiceImpl.getBook(cart.getBookId());
        //获取当前时间，并将时间赋值于cart对象的创建时间和修改时间
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        List<Cart> carts = cartServiceImpl.listCart();
        //购物车中是否有相同的书籍商品
        boolean flag = false;
        for (Cart oldCart : carts) {
            //将前端传递过来的cart信息的图书Id与数据库中cart中图书Id进行比较，
            // 当前用户Id与数据库中cart中用户Id进行比较，两者全相等，则说明此cart已存在
            //如果cart存在，跳出循环
            if (cart.getBookId().equals(oldCart.getBookId()) && userId.equals(oldCart.getUserId())){
                flag = true;
                break;
            }
        }
        //cart存在
        if (flag){
            //根据用户Id和图书Id查询cart信息
            Cart existCart = cartServiceImpl.getCartByUserAndBookId(userId, book.getId());
            //购物车中已存在的购买数量
            int existNum = existCart.getNum();
            //新增的购买数量
            int addNum = cart.getNum();
            //新的购买数量
            int num = existNum + addNum;
            existCart.setNum(num);
            existCart.setUpdateTime(timestamp);
            cartServiceImpl.updateCart(existCart);
        }else {
            //cart不存在
            //创建一个新的cart对象，封装从前端传递过来的数据
            Cart newCart = new Cart(userId, book.getId(), book.getName(), book.getPrice(), cart.getNum(), book.getAuthor(), book.getNum(), timestamp, timestamp);
            cartServiceImpl.insertCart(newCart);
        }
        //发送延时消息到延迟队列上
        cartServiceImpl.sendDelayQueue(userId, book.getId());
        //跳转到查询cart的方法
        return "redirect:/user/listCart";
    }

    /**
     * 获取所有购物车信息
     * @param model
     * @param response
     * @param pageNum
     * @return
     */
    @RequestMapping("/listCart")
    public String listCart(Model model, HttpServletResponse response, Integer pageNum) {
        //判断前端传递过来的pageNum是否为空
        if (pageNum == null){
            pageNum = 1;
        }
        Book book = null;
        //创建一个Pageable对象用于封装pageNUm和每页显示数据数量
        // （当前页，每页记录数）
        Pageable pageable = PageRequest.of(pageNum - 1, ConstantUtil.PAGE_RECORD_NUM);
        //根据pageable对象查询当前登录用户的购物车信息
        Page<Cart> list = cartServiceImpl.findAll(pageable);
        //单个商品总价
        int totalPrice = 0;
        //购物车中商品总价
        int allPrice = 0;
        for (Cart cart : list) {
            book = bookServiceImpl.getBook(cart.getBookId());
            //设置cart中图书库存
            cart.setStock(book.getNum());
            //购买数量
            int num = cart.getNum();
            //图书售价格式转换
            BigDecimal bookPrice = cart.getPrice();
            int price = bookPrice.intValue();
            //计算购物车中单个商品总价
            totalPrice = num*price;
            //计算购物车中商品总价
            allPrice = allPrice+totalPrice;
        }
        model.addAttribute("carts", list);
        model.addAttribute("allPrice", allPrice);
        // 允许iframe
        response.addHeader("x-frame-options","SAMEORIGIN");
        //跳转到cart界面
        return "user/listCart";
    }
    /**
     * 根据图书Id删除图书
     * @param id
     * @return
     */
    @GetMapping("/deleteCart/{id}")
    public String deleteCart(@PathVariable("id")Integer id){
        cartServiceImpl.deleteCart(id);
        //跳转到查询cart的方法
        return "redirect:/user/listCart";
    }
    /**
     * 根据图书Id删除图书
     * @param session
     * @return
     */
    @GetMapping("/deleteAllCart")
    public String deleteAllCart(HttpSession session){
        //获取session的登录用户Id
        int userId = (int) session.getAttribute("userId");
        //根据用户Id删除cart
        cartServiceImpl.deleteByUserId(userId);
        //跳转到查询cart的方法
        return "redirect:/user/listCart";
    }

    /**
     * 结算
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/addOrder")
    public String addOrder(Model model,HttpSession session){
        //获取session中当前登录用户Id
        int userId = (int) session.getAttribute("userId");
        //根据Id查询对应购物车信息
        List<Cart> carts = cartServiceImpl.getCart(userId);
        for (Cart cart : carts) {
            Book book = bookServiceImpl.getBook(cart.getBookId());
            Integer num1 = cart.getNum();
            Integer num = book.getNum();
            //减少对应商品库存数量
            num = num -num1;
            book.setNum(num);
            //赋予图书对象修改时间为当前时间
            book.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));
            //修改图书
            bookServiceImpl.updateBook(book);
        }
        //跳转到删除所有cart数据方法
        return "redirect:/user/deleteAllCart";
    }
}
