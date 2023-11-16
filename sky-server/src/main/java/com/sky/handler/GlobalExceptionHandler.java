package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }
//    SQLIntegrityConstraintViolationException: Duplicate entry 'lisi' for key 'idx_username'
        @ExceptionHandler
        public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
            log.error("异常信息：{}", ex.getMessage());
             String messages = ex.getMessage();
            if (messages.contains("Duplicate entry")){
                String[] message = messages.split(" ");
                 String name = message[2];
                String  msg=name+ MessageConstant.ALREADY_EXISTS;
                return Result.error(msg);
            }else {
            return Result.error("未知错误");}
        }
}
