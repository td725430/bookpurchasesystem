package com.tenton.controller;

import com.mysql.cj.util.StringUtils;
import com.tenton.pojo.Book;
import com.tenton.service.impl.BookServiceImpl;
import com.tenton.utils.ConstantUtil;
import com.tenton.utils.POIUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @Date: 2021/1/29
 * @Author: Tenton
 * @Description:
 */
@Controller
@RequestMapping("/admin")
public class BookController {
    @Autowired
    BookServiceImpl bookServiceImpl;

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
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        book.setCreateTime(timestamp);
        book.setUpdateTime(timestamp);
        bookServiceImpl.insertBook(book);
        return "redirect:/admin/listBook";
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
     * 根据图书Id查询图书信息，并将对应书籍传递给前端界面
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
        book.setCreateTime(b.getCreateTime());
        book.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));
        bookServiceImpl.updateBook(book);
        return "redirect:/admin/listBook";
    }

    /**
     * 根据图书Id删除图书
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable("id")Integer id, Model model){
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
            List<String[]> list = POIUtil.readExcel(file);
            if(list !=null && list.size() >0){
                List<Book> data = new ArrayList<>();
                for (String[] strings : list) {
                    String name = strings[0];
                    String priceStr = strings[1];
                    Integer priceInt = Integer.parseInt(priceStr);
                    BigDecimal price = new BigDecimal(priceInt);
                    Integer num = Integer.parseInt(strings[2]);
                    String author = strings[3];
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Book orderSetting = new Book(name,author,price,num,timestamp,timestamp);
                    data.add(orderSetting);
                }
                bookServiceImpl.addBook(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/listBook";
    }

}
