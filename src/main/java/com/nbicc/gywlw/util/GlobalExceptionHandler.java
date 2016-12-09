package com.nbicc.gywlw.util;

import com.nbicc.gywlw.Model.ErrorInfo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by BigMao on 2016/12/9.
 * 全局异常（错误）处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public ErrorInfo<String> jsonErrorHandler(HttpServletRequest req, MyException e) throws Exception {
        ErrorInfo<String> r = new ErrorInfo<>();
        r.setMessage(e.getMessage());
        r.setResult_code(ErrorInfo.ERROR);
        r.setData("Error");
        r.setUrl(req.getRequestURL().toString());
        return r;
    }
}
