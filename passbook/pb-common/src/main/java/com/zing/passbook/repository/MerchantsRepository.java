package com.zing.passbook.repository;

import com.zing.passbook.entity.Merchants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Zing
 * @date 2019-11-27
 */
public interface MerchantsRepository extends JpaRepository<Merchants, Long> {

    /**
     * <h2>根据商户名称获取商户对象</h2>
     *
     * @param name 商户名称
     * @return {@link Merchants}
     */
    Merchants findByName(String name);

    /**
     * <h2>根据商户 ids 获取商户对象</h2>
     *
     * @param ids 商户 ids
     * @return {@link Merchants}
     */
    List<Merchants> findByIdIn(List<Long> ids);
}
