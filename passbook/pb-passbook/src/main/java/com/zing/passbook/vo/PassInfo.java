package com.zing.passbook.vo;

import com.zing.passbook.entity.Merchants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户领取的优惠券信息
 *
 * @author Zing
 * @date 2019-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassInfo {

    /**
     * 优惠券
     */
    private Pass pass;

    /**
     * 优惠券模板
     */
    private PassTemplate passTemplate;

    /**
     * 优惠券对应的商户
     */
    private Merchants merchants;
}
