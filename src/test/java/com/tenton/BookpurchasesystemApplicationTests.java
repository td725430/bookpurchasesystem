package com.tenton;

import com.tenton.pojo.Book;
import com.tenton.pojo.Cart;
import com.tenton.pojo.User;
import com.tenton.service.impl.BookServiceImpl;
import com.tenton.service.impl.CartServiceImpl;
import com.tenton.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@SpringBootTest
class BookpurchasesystemApplicationTests {
    @Autowired
    BookServiceImpl bookService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    CartServiceImpl cartService;
    @Test
    void contextLoads() {
    }
    @Test
    public void testSave(){
//        BigDecimal bigDecimal = new BigDecimal(23.5);
//        Book book = new Book("SpringDataJPA","east",bigDecimal,50);
//        bookService.insertBook(book);
    }
    @Test
    public void testUserSave(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        User user = new User("zs","123456","east","923004696@qq.com","12345678947","成都","家庭住址","成都",timestamp,timestamp);
        userService.insertUser(user);
        System.out.println(userService.listUser());
    }
    @Test
    public void testCart(){
        List<Cart> carts = cartService.getCart(7);
        for (Cart cart : carts) {
            System.out.println(cart);
        }
    }
    @Test
    public void testEmail(){
        userService.sendEmail("923004696@qq.com");
    }
}
