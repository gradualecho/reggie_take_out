package com.itheima.reggie.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;

@ResponseBody
@ControllerAdvice
public class ReggieException {

    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class})
    public R<String> ex(SQLIntegrityConstraintViolationException ex){
        if (ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split("'");
            return R.error(split[1]+"已注册");
        }
        return R.error("未知错误");
    }
}
