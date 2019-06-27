package com.rjsoft.magina.component.query.sqlFile;

import com.rjsoft.magina.component.query.jpa.BeanTransformerAdapter;
import com.rjsoft.magina.component.query.jpa.QueryBuilder;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据Sql 查询
 */
public class BaseDynSqlServiceImpl implements BaseDynSqlService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 分页查询返回List
     *
     * @param sql       查询sql
     * @param pageable  分页
     * @param beanOrMap 查询参数
     * @return 查询结果list
     */
    @Override
    public List queryBySQL(String sql, Pageable pageable, Object beanOrMap) {
        Map<String, Object> params = getParamMap(beanOrMap);
        Session session = this.entityManager.unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(sql);

        QueryBuilder.setParams(query, params);

        if (pageable != null) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }

        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    /**
     * 查询全部
     *
     * @param sql       查询sql
     * @param cla       Dto
     * @param beanOrMap 参数
     * @param <M>       Dto
     * @return 列表
     */
    @Override
    public <M> List queryAllBySQL(String sql, Class<M> cla, Object beanOrMap) {

        Map<String, Object> params = getParamMap(beanOrMap);

        Session session = this.entityManager.unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(sql);
        QueryBuilder.setParams(query, params);

        query.unwrap(NativeQueryImpl.class).setResultTransformer(new BeanTransformerAdapter<M>(cla));

        return query.getResultList();
    }

    @Override
    public Object getSingleResult(String sql, Class cla, Object beanOrMap) {

        Map<String, Object> params = getParamMap(beanOrMap);

        Session session = this.entityManager.unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(sql);
        QueryBuilder.setParams(query, params);

        query.unwrap(NativeQueryImpl.class).setResultTransformer(new BeanTransformerAdapter(cla));

        return query.getSingleResult();
    }

    @Override
    public Object getSingleResult(String sql, Object beanOrMap) {

        Map<String, Object> params = getParamMap(beanOrMap);

        Session session = this.entityManager.unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(sql);
        QueryBuilder.setParams(query, params);

        return query.getSingleResult();
    }

    /**
     * sql 类型是更新 和 插入
     *
     * @param sql       sql
     * @param beanOrMap 参数
     */
    @Override
    @Transactional
    public Integer executeSql(String sql, Object beanOrMap) {
        Map<String, Object> params = getParamMap(beanOrMap);

        Session session = this.entityManager.unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(sql);
        QueryBuilder.setParams(query, params);
        return query.executeUpdate();
    }

    @Override
    public <M> Page<M> queryBySQLPage(String sql, Pageable pageable, Class<M> cla, Object beanOrMap) {

        Map<String, Object> params = getParamMap(beanOrMap);

        Long count = queryCountBySelect(sql, params);

        if (count == null || count == 0) {
            return new PageImpl<M>(new ArrayList<>(), pageable, 0);
        }

        Session session = this.entityManager.unwrap(Session.class);
        NativeQuery query = session.createNativeQuery(sql);
        QueryBuilder.setParams(query, params);

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        query.unwrap(NativeQueryImpl.class).setResultTransformer(new BeanTransformerAdapter<M>(cla));

        return new PageImpl<M>(query.getResultList(), pageable, count);
    }

    protected Map<String, Object> getParamMap(Object beanOrMap) {
        Map<String, Object> params;
        if (beanOrMap == null) {
            beanOrMap = new HashMap<>();
        }

        if (beanOrMap instanceof Map) {
            params = (Map<String, Object>) beanOrMap;
        } else {
            params = QueryBuilder.toMap(beanOrMap);
        }
        return params;
    }

    /**
     * 根据sql 转化成 count sql 并查询 count
     *
     * @param selectSql 查询sql
     * @param params    查询参数
     * @return 总条数
     */
    public Long queryCountBySelect(String selectSql, final Map<String, Object> params) {

        Session session = this.entityManager.unwrap(Session.class);
        NativeQuery queryCount = session.createNativeQuery(QueryBuilder.toCountQuery(selectSql));

        QueryBuilder.setParams(queryCount, params);

        Object count = queryCount.getSingleResult();
        if (count != null) {
            return Long.valueOf(count.toString());
        }

        return null;
    }

    public Long queryCountBySql(String countSql, final Map<String, Object> params) {

        Session session = this.entityManager.unwrap(Session.class);
        NativeQuery queryCount = session.createNativeQuery(countSql);

        QueryBuilder.setParams(queryCount, params);

        Object count = queryCount.getSingleResult();
        if (count != null) {
            return Long.valueOf(count.toString());
        }
        return null;
    }

}
