package com.tenton.service;

import com.tenton.pojo.Book;
import com.tenton.utils.PageResult;
import com.tenton.utils.QueryPageBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Date: 2021/1/29
 * @Author: Tenton
 * @Description: 图书
 */
public interface BookService {
    /**
     * 增加图书
     * @param book
     * @return
     */
    void insertBook(Book book);

    /**
     * 批量插入图书
     * @param books
     */
    void addBook(List<Book> books);

    /**
     * 删除图书
     * @param id
     * @return
     */
    void deleteBook(int id);

    /**
     * 修改图书
     * @param book
     * @return
     */
    void updateBook(Book book);

    /**
     * 获取所有图书
     * @return
     */
    List<Book> listBook();

    /**
     * 通过图书Id查询图书
     * @param id
     * @return
     */
    Book getBook(int id);

    /**
     * 分页查询
     * @param pageable
     * @return
     */
    Page<Book> findAll(Pageable pageable);

    /**
     * 模糊查询
     * @param name
     * @param pageNum
     * @return
     */
    Page<Book> pageQuery(String name, Integer pageNum);

}
