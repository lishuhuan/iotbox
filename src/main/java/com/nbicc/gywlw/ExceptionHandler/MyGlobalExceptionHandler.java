package com.nbicc.gywlw.ExceptionHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by BigMao on 2016/12/9.
 * 全局异常（错误）处理
 */
@ControllerAdvice
public class MyGlobalExceptionHandler {
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


//    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
//    public JSONObject defaultErrorHandler(HttpServletRequest req, Exception e) {
//        e.printStackTrace();
//        System.out.println("GlobalDefaultExceptionHandler");
//        return MyUtil.response(-2,"错误");
//    }

}
