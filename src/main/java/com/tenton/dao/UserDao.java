package com.tenton.dao;

/**
 * @Date: 2021/1/29
 * @Author: Tenton
 * @Description: 用户操作数据接口
 */

import com.tenton.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 参数一 T :当前需要映射的实体
 * 参数二 ID :当前映射的实体中的OID的类型
 *
 */
public interface UserDao extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User> {
}
