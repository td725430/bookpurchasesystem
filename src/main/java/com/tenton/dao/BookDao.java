package com.tenton.dao;

import com.tenton.pojo.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Date: 2021/1/29
 * @Author: Tenton
 * @Description:数据操作数据接口
 */
/**
 * 参数一 T :当前需要映射的实体
 * 参数二 ID :当前映射的实体中的OID的类型
 *
 */
public interface BookDao extends JpaRepository<Book,Integer>, JpaSpecificationExecutor<Book> {
}
