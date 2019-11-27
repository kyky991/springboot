package com.zing.passbook.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zing
 * @date 2019-11-27
 */
@Getter
@AllArgsConstructor
public enum FeedbackType {

    PASS("pass", "针对优惠券的评论"),
    APP("app", "针对卡包 App 的评论"),

    ;

    /**
     * 评论类型编码
     */
    private String code;

    /**
     * 评论类型描述
     */
    private String desc;

}
