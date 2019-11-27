package com.zing.passbook.vo;

import com.zing.passbook.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zing
 * @date 2019-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

    /**
     * 错误码, 正确返回 0
     */
    private Integer code = 0;

    /**
     * 错误信息, 正确返回空字符串
     */
    private String msg = "";

    /**
     * 返回值对象
     */
    private T data;

    /**
     * <h2>正确的响应构造函数</h2>
     *
     * @param data 返回值对象
     */
    public Response(T data) {
        this.data = data;
    }

    public Response(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getDesc();
    }

    public static Response success() {
        return build(ResultCode.SUCCESS);
    }

    public static <T> Response<T> success(T data) {
        return build(ResultCode.SUCCESS, data);
    }

    public static Response error() {
        return build(ResultCode.ERROR);
    }

    public static Response error(String msg) {
        return new Response<>(ResultCode.ERROR.getCode(), msg, null);
    }

    public static <T> Response<T> build(ResultCode resultCode) {
        return new Response<>(resultCode);
    }

    public static <T> Response<T> build(ResultCode resultCode, T data) {
        Response<T> response = new Response<>(resultCode);
        response.setData(data);
        return response;
    }

}
