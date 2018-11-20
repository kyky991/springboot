package com.zing.boot.fileupload.service.impl;

import com.zing.boot.fileupload.service.IBaseService;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BaseServiceImpl implements IBaseService {

    @PersistenceContext
    private EntityManager em;

    public List findByHql(String hql, Object[] param, Pageable pageable) {
        Query query = em.createQuery(hql);
        query.setFirstResult(pageable.getPageNumber());
        query.setMaxResults(pageable.getPageSize());
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; ++i) {
                query.setParameter(i, param[i]);
            }
        }

        return query.getResultList();
    }

    public List findByHql(String hql, Map<String, Object> params) {
        Query query = em.createQuery(hql);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query.getResultList();
    }

    @Override
    public List findBySql(String sql, Map<String, Object> params) {
        Query query = em.createNativeQuery(sql);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public Page findPageByHql(String hql, Object[] param, String countHql, Object[] countParam, Pageable pageable) {
        Query countQuery = em.createQuery(countHql);
        if (countParam != null && countParam.length > 0) {
            for (int i = 0; i < countParam.length; ++i) {
                countQuery.setParameter(i, countParam[i]);
            }
        }
        Long lcount = null;
        try {
            lcount = (Long) countQuery.getSingleResult();
        } catch (NoResultException e) {

        }
        long count = lcount == null ? 0 : lcount;
        Query query = em.createQuery(hql);
        query.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
        query.setMaxResults(pageable.getPageSize());
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; ++i) {
                query.setParameter(i, param[i]);
            }
        }
        List list = query.getResultList();

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Page page = new Page() {
            @Override
            public int getTotalPages() {
                return (int) (count / pageSize + count % pageSize == 0 ? 0 : 1);
            }

            @Override
            public long getTotalElements() {
                return count;
            }

            @Override
            public Page map(Converter converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return pageNumber;
            }

            @Override
            public int getSize() {
                return pageSize;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List getContent() {
                return list;
            }

            @Override
            public boolean hasContent() {
                return list != null && list.size() > 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return pageNumber == 0;
            }

            @Override
            public boolean isLast() {
                return pageNumber == getTotalPages();
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return null;
            }
        };
        return page;
    }

    public Page findPageByHql(String hql, Map<String, Object> param, String countHql, Map<String, Object> countParam, Pageable pageable) {
        Query countQuery = em.createQuery(countHql);
        if (countParam != null && countParam.size() > 0) {
            for (Map.Entry<String, Object> entry : countParam.entrySet()) {
                countQuery.setParameter(entry.getKey(), entry.getValue());
            }
        }
        long count = (long) countQuery.getSingleResult();
        Query query = em.createQuery(hql);
        query.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
        query.setMaxResults(pageable.getPageSize());
        if (param != null && param.size() > 0) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        List list = query.getResultList();

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Page page = new Page() {
            @Override
            public int getTotalPages() {
                return (int) (count / pageSize + count % pageSize == 0 ? 0 : 1);
            }

            @Override
            public long getTotalElements() {
                return count;
            }

            @Override
            public Page map(Converter converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return pageNumber;
            }

            @Override
            public int getSize() {
                return pageSize;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List getContent() {
                return list;
            }

            @Override
            public boolean hasContent() {
                return list != null && list.size() > 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return pageNumber == 0;
            }

            @Override
            public boolean isLast() {
                return pageNumber == getTotalPages();
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return null;
            }
        };
        return page;
    }


    public Page findPageBySql(String sql, Map<String, Object> param, String countSql, Map<String, Object> countParam, Pageable pageable) {
        Query countQuery = em.createNativeQuery(countSql);
        if (countParam != null && countParam.size() > 0) {
            for (Map.Entry<String, Object> entry : countParam.entrySet()) {
                countQuery.setParameter(entry.getKey(), entry.getValue());
            }
        }
        BigInteger bigInteger = (BigInteger) countQuery.getSingleResult();
        long count = Long.parseLong(bigInteger.toString());
        Query query = em.createNativeQuery(sql);
        query.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
        query.setMaxResults(pageable.getPageSize());
        if (param != null && param.size() > 0) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = query.getResultList();

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Page page = new Page() {
            @Override
            public int getTotalPages() {
                return (int) (count / pageSize + count % pageSize == 0 ? 0 : 1);
            }

            @Override
            public long getTotalElements() {
                return count;
            }

            @Override
            public Page map(Converter converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return pageNumber;
            }

            @Override
            public int getSize() {
                return pageSize;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List getContent() {
                return list;
            }

            @Override
            public boolean hasContent() {
                return list != null && list.size() > 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return pageNumber == 0;
            }

            @Override
            public boolean isLast() {
                return pageNumber == getTotalPages();
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return null;
            }
        };
        return page;
    }

    @Override
    public void excuteSql(String sql, Map<String, Object> params) {
        Query query = em.createNativeQuery(sql);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        query.executeUpdate();
    }
}
