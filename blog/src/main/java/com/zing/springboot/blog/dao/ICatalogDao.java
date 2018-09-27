package com.zing.springboot.blog.dao;

import java.util.List;

import com.zing.springboot.blog.pojo.Catalog;
import com.zing.springboot.blog.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Catalog 仓库.
 */
public interface ICatalogDao extends JpaRepository<Catalog, Long>{
	
	/**
	 * 根据用户查询
	 * @param user
	 * @return
	 */
	List<Catalog> findByUser(User user);
	
	/**
	 * 根据用户查询
	 * @param user
	 * @param name
	 * @return
	 */
	List<Catalog> findByUserAndName(User user, String name);
}
