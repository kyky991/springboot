package com.zing.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户领取优惠券的请求对象
 *
 * @author Zing
 * @date 2019-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GainPassTemplateRequest {

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * PassTemplate 对象
     */
    private PassTemplate passTemplate;
}
