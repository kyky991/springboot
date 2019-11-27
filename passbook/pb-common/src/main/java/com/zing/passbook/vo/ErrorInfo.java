package com.zing.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一的错误信息
 *
 * @author Zing
 * @date 2019-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfo<T> {

    /**
     * 错误码
     */
    public static final Integer ERROR = -1;

    /**
     * 特定错误码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 请求 url
     */
    private String url;

    /**
     * 请求返回的数据
     */
    private T data;

}
