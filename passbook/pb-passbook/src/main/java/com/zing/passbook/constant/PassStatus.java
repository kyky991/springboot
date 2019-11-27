package com.zing.passbook.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 优惠券的状态
 *
 * @author Zing
 * @date 2019-11-27
 */
@Getter
@AllArgsConstructor
public enum PassStatus {

    UNUSED(1, "未被使用的"),
    USED(2, "已经使用的"),
    ALL(3, "全部领取的"),

    ;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态描述
     */
    private String desc;

}
