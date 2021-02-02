package com.tenton.service.impl;

import com.mysql.cj.util.StringUtils;
import com.tenton.dao.BookDao;
import com.tenton.pojo.Book;
import com.tenton.service.BookService;
import com.tenton.utils.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @Date: 2021/1/29
 * @Author: Tenton
 * @Description: 图书
 */
@Service
public class BookServiceImpl implements BookService {
    @Autowired
    BookDao bookDao;

    /**
     * 增加图书
     * @param book
     * @return
     */
    @Override
    public void insertBook(Book book) {
        bookDao.save(book);
    }
    /**
     * 批量插入图书
     * @param books
     */
    @Override
    public void addBook(List<Book> books) {
        bookDao.saveAll(books);
    }

    /**
     * 删除图书
     * @param id
     * @return
     */
    @Override
    public void deleteBook(int id) {
        bookDao.deleteById(id);
    }

    /**
     * 修改图书
     * @param book
     * @return
     */
    @Override
    public void updateBook(Book book) {
        bookDao.save(book);
    }

    /**
     * 获取所有图书
     * @return
     */
    @Override
    public List<Book> listBook() {
        return bookDao.findAll();
    }
    /**
     * 通过图书Id查询图书
     * @param id
     * @return
     */
    @Override
    public Book getBook(int id) {
        return bookDao.getOne(id);
    }
    /**
     * 模糊查询
     * @param name
     * @param pageNum
     * @return
     */
    @Override
    public Page<Book> pageQuery(String name, Integer pageNum) {
        Specification<Book>  specification = new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = null;
                //判断你是否输入了查询条件，如果有条件，就通过接口拼装sql进行模糊查询
                if (!StringUtils.isNullOrEmpty(name)) {
                    predicate = cb.like(root.get("name").as(String.class), "%" + name + "%");
                }
                return predicate;
            }
        };
        Pageable pageable = PageRequest.of(pageNum-1, ConstantUtil.PAGE_RECORD_NUM);
        Page<Book> list = bookDao.findAll(specification, pageable);
        return list;
    }

    /**
     * 分页查询
     * @param pageable
     * @return
     */
    @Override
    public Page<Book> findAll(Pageable pageable) {
        return bookDao.findAll(pageable);
    }
}
