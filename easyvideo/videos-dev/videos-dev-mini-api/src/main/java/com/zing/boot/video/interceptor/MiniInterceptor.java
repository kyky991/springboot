package com.zing.boot.video.interceptor;

import com.zing.boot.video.utils.JSONResult;
import com.zing.boot.video.utils.JsonUtils;
import com.zing.boot.video.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class MiniInterceptor implements HandlerInterceptor {

    public static final String USER_REDIS_SESSION = "user-redis-session";

    @Autowired
    public RedisOperator redis;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("headerUserId");
        String userToken = request.getHeader("headerUserToken");

        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)) {
            String uniqueToken = redis.get(USER_REDIS_SESSION + ":" + userId);
            if (StringUtils.isEmpty(uniqueToken) && StringUtils.isBlank(uniqueToken)) {
                System.out.println("请登录...");
                returnErrorResponse(response, new JSONResult().errorTokenMsg("请登录..."));
                return false;
            } else {
                if (!uniqueToken.equals(userToken)) {
                    System.out.println("账号被挤出...");
                    returnErrorResponse(response, new JSONResult().errorTokenMsg("账号被挤出..."));
                    return false;
                }
            }
        } else {
            System.out.println("请登录...");
            returnErrorResponse(response, new JSONResult().errorTokenMsg("请登录..."));
            return false;
        }


        /**
         * 返回 false：请求被拦截，返回
         * 返回 true ：请求OK，可以继续执行，放行
         */
        return true;
    }

    public void returnErrorResponse(HttpServletResponse response, JSONResult result)
            throws IOException {
        OutputStream out = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 请求controller之后，渲染视图之前
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 请求controller之后，视图渲染之后
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
