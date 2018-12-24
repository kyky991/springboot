package com.zing.boot.sell.service;

import com.zing.boot.sell.pojo.ProductCategory;

import java.util.List;

public interface ICategoryService {

    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);
}
