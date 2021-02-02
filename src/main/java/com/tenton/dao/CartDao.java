package com.tenton.dao;

/**
 * @Date: 2021/1/29
 * @Author: Tenton
 * @Description:仓库操作数据接口
 */

import com.tenton.pojo.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 参数一 T :当前需要映射的实体
 * 参数二 ID :当前映射的实体中的OID的类型
 *
 */
public interface CartDao extends JpaRepository<Cart,Integer>, JpaSpecificationExecutor<Cart> {
    @Modifying
    @Query(value = "delete from Cart where userId = ?1")
    void deleteAllByUserId(Integer userId);
    @Query(value = "select id,userId,bookId,name,price,num,author,stock,createTime,updateTime from Cart where userId = ?1")
    List<Cart> findAllByUserId(Integer userId);
}
