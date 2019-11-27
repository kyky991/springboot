package com.zing.passbook.advice;

import com.zing.passbook.vo.ErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Zing
 * @date 2019-11-27
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ErrorInfo<String> errorHandler(HttpServletRequest request, Exception e) {
        ErrorInfo<String> info = new ErrorInfo<>();
        info.setCode(ErrorInfo.ERROR);
        info.setMsg(e.getMessage());
        info.setData("Do Not Have Return Data");
        info.setUrl(request.getRequestURL().toString());

        log.error(e.getMessage(), e);

        return info;
    }

}
