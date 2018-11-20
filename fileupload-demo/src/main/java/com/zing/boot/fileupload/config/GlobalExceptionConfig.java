package com.zing.boot.fileupload.config;

import com.zing.boot.fileupload.dto.ResponseEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionConfig.class);

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEx runtimeExceptionHandler(Exception e) {
        LOGGER.error("---------> huge error!", e);
        return new ResponseEx().error(ErrorCode.ERROR,e.getMessage());
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEx illegalParamsExceptionHandler(IllegalArgumentException e) {
        LOGGER.error("---------> invalid request!", e);
        return new ResponseEx().error(ErrorCode.ILLEGAL_ARGUMENT_ERROR,e.getMessage());
    }

}
