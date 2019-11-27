package com.zing.passbook.vo;

import com.zing.passbook.entity.Merchants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 优惠券模板信息
 *
 * @author Zing
 * @date 2019-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassTemplateInfo {

    /**
     * 优惠券模板
     */
    private PassTemplate passTemplate;

    /**
     * 优惠券对应的商户
     */
    private Merchants merchants;
}
