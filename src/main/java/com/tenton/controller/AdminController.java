package com.tenton.controller;

import com.tenton.pojo.Book;
import com.tenton.service.impl.BookServiceImpl;
import com.tenton.utils.ConstantUtil;
import com.tenton.utils.POIUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @Date: 2021/1/29
 * @Author: Tenton
 * @Description: 管理员对书籍的管理
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    BookServiceImpl bookServiceImpl;

    /**
     * 跳转到新增图书界面
     * @return
     */
    @RequestMapping("/toAddBook")
    public String toAddBook(){
        return "/admin/addBook";
    }
    /**
     * 添加图书
     * @param book
     * @return
     */
    @PostMapping("/addBook")
    public String addBook(Book book){
        //获取当前时间
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //给图书对象创建时间和修改时间赋予当前时间
        book.setCreateTime(timestamp);
        book.setUpdateTime(timestamp);
        //新增
        bookServiceImpl.insertBook(book);
        //重定向到当前controller中的listBook方法
        return "redirect:/admin/listBook";
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
        Page<Book> list = bookServiceImpl.findAll(pageable);
        model.addAttribute("books", list);
        // 允许iframe
        response.addHeader("x-frame-options","SAMEORIGIN");
        //跳转到管理员模块下的图书界面
        return "admin/list";
    }

    /**
     * 跳转到查询图书界面
     * @return
     */
    @GetMapping("/toGetBook")
    public String toGetBook(){
        return "admin/getBook";
    }
    /**
     * 根据图书查询图书信息，并将对应书籍传递给前端界面
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
        //将查询结果添加到model域中传递给前端
        model.addAttribute("bookList", list);
        // 允许iframe
        response.addHeader("x-frame-options","SAMEORIGIN");
        return "admin/getBook";
    }
    /**
     * 根据图书Id查询图书信息，并将对应书籍传递给前端修改界面
     * @return
     */
    @GetMapping("/toUpdateBook/{id}")
    public String toUpdateBook(@PathVariable("id")Integer id, Model model){
        //根据图书Id查询图书
        Book book = bookServiceImpl.getBook(id);
        model.addAttribute("book",book);
        return "admin/updateBook";
    }
    /**
     * 修改图书
     * @return
     */
    @PostMapping("/updateBook")
    public String updateBook(Book book){
        Book b = bookServiceImpl.getBook(book.getId());
        //修改时创建时间不用变更，直接获取已有并赋值就行
        book.setCreateTime(b.getCreateTime());
        //获取当前时间并赋值给修改时间
        book.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));
        bookServiceImpl.updateBook(book);
        return "redirect:/admin/listBook";
    }

    /**
     * 根据图书Id删除图书
     * @param id
     * @return
     */
    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable("id")Integer id){
        bookServiceImpl.deleteBook(id);
        return "redirect:/admin/listBook";
    }

    /**
     * 跳转到批量上传界面
     * @return
     */
    @GetMapping("/toUploadExcel")
    public String toUploadExcel(){
        return "admin/uploadExcel";
    }

    /**
     * 批量添加图书
     * @param file
     */
    @RequestMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file){
        try {
            //解析上传的excel文档，解析成list集合
            List<String[]> list = POIUtil.readExcel(file);
            //判断excel文件中是否有数据
            if(list !=null && list.size() >0){
                List<Book> bookList = new ArrayList<>();
                //遍历，获取文件中对应的值，并赋值给一个新图书对象
                for (String[] strings : list) {
                    String name = strings[0];
                    //图书售价，进行格式转换
                    BigDecimal price = new BigDecimal(Integer.parseInt(strings[1]));
                    //库存
                    Integer num = Integer.parseInt(strings[2]);
                    //作者
                    String author = strings[3];
                    //获取当前时间
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    //将获取到的值封装成一个Book对象
                    Book newBook = new Book(name,author,price,num,timestamp,timestamp);
                    bookList.add(newBook);
                }
                bookServiceImpl.addBook(bookList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //跳转到查询所有图书信息方法
        return "redirect:/admin/listBook";
    }

}
