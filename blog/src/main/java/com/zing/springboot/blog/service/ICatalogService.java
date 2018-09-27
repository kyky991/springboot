package com.zing.springboot.blog.service;

import com.zing.springboot.blog.pojo.Catalog;
import com.zing.springboot.blog.pojo.User;

import java.util.List;


/**
 * Catalog 服务接口.
 */
public interface ICatalogService {
    /**
     * 保存Catalog
     *
     * @param catalog
     * @return
     */
    Catalog saveCatalog(Catalog catalog);

    /**
     * 删除Catalog
     *
     * @param id
     * @return
     */
    void removeCatalog(Long id);

    /**
     * 根据id获取Catalog
     *
     * @param id
     * @return
     */
    Catalog getCatalogById(Long id);

    /**
     * 获取Catalog列表
     *
     * @return
     */
    List<Catalog> listCatalogs(User user);
}
