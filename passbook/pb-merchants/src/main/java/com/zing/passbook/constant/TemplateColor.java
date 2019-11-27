package com.zing.passbook.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zing
 * @date 2019-11-27
 */
@Getter
@AllArgsConstructor
public enum TemplateColor {

    RED(1, "红色"),
    GREEN(2, "绿色"),
    BLUE(3, "蓝色");

    /**
     * 颜色代码
     */
    private Integer code;

    /**
     * 颜色描述
     */
    private String color;
}
