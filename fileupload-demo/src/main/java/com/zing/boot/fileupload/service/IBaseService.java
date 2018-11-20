package com.zing.boot.fileupload.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IBaseService {
    List findByHql(String hql, Object[] param, Pageable pageable);

    List findByHql(String hql, Map<String, Object> params);

    List findBySql(String sql, Map<String, Object> params);

    Page findPageByHql(String hql, Object[] param, String countHql, Object[] countParam, Pageable pageable);

    Page findPageByHql(String hql, Map<String, Object> param, String countHql, Map<String, Object> countParam, Pageable pageable);

    Page findPageBySql(String sql, Map<String, Object> param, String countHql, Map<String, Object> countParam, Pageable pageable);

    void excuteSql(String sql, Map<String, Object> params);
}
