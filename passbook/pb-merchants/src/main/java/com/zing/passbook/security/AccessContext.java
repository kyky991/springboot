package com.zing.passbook.security;

/**
 * 用 ThreadLocal 去单独存储每一个线程携带的 Token 信息
 *
 * @author Zing
 * @date 2019-11-27
 */
public class AccessContext {

    private static final ThreadLocal<String> TOKEN = new ThreadLocal<>();

    public static String getToken() {
        return TOKEN.get();
    }

    public static void setToken(String token) {
        TOKEN.set(token);
    }

    public static void clearAccessKey() {
        TOKEN.remove();
    }

}
