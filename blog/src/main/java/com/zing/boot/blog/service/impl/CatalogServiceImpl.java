package com.zing.boot.blog.service.impl;

import java.util.List;

import com.zing.boot.blog.pojo.Catalog;
import com.zing.boot.blog.pojo.User;
import com.zing.boot.blog.service.ICatalogService;
import com.zing.boot.blog.dao.ICatalogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


/**
 * Catalog 服务.
 */
@Service
public class CatalogServiceImpl implements ICatalogService {

	@Autowired
	private ICatalogDao catalogDao;

	@Transactional
	@Override
	public Catalog saveCatalog(Catalog catalog) {
		// 判断重复
		List<Catalog> list = catalogDao.findByUserAndName(catalog.getUser(), catalog.getName());
		if(list !=null && list.size() > 0) {
			throw new IllegalArgumentException("该分类已经存在了");
		}
		return catalogDao.save(catalog);
	}

	@Transactional
	@Override
	public void removeCatalog(Long id) {
		catalogDao.delete(id);
	}

	@Override
	public Catalog getCatalogById(Long id) {
		return catalogDao.findOne(id);
	}

	@Override
	public List<Catalog> listCatalogs(User user) {
		return catalogDao.findByUser(user);
	}

}
