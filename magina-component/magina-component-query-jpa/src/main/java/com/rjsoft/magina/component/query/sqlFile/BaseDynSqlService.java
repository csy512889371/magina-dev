package com.rjsoft.magina.component.query.sqlFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseDynSqlService {


    /**
     * 分页查询返回List
     *
     * @param sql       查询sql
     * @param pageable  分页
     * @param beanOrMap 查询参数
     * @return 查询结果list
     */
    List queryBySQL(String sql, Pageable pageable, Object beanOrMap);


    /**
     * 查询全部
     *
     * @param sql       查询sql
     * @param cla       Dto
     * @param beanOrMap 参数
     * @param <M>       Dto
     * @return 列表
     */
    <M> List queryAllBySQL(String sql, Class<M> cla, Object beanOrMap);

    /**
     * @param sql       查询的sql
     * @param pageable  分页参数
     * @param cla       Dto
     * @param beanOrMap 查询参数
     * @param <M>       Dto
     * @return
     */
    <M> Page<M> queryBySQLPage(String sql, Pageable pageable, Class<M> cla, Object beanOrMap);

    /**
     * 获取单个结果
     *
     * @param sql       sql
     * @param beanOrMap 参数
     * @return 结果值
     */
    Object getSingleResult(String sql, Object beanOrMap);

    /**
     * sql 类型是更新 和 插入
     *
     * @param sql       sql
     * @param beanOrMap 参数
     */
    Integer executeSql(String sql, Object beanOrMap);

    /**
     * sql 类型是更新 和 插入
     *
     * @param sql       sql
     * @param cla       cla
     * @param beanOrMap 参数
     */
    Object getSingleResult(String sql, Class cla, Object beanOrMap);

}
