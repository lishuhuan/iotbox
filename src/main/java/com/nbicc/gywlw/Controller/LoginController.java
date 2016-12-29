package com.nbicc.gywlw.Controller;


import com.alibaba.fastjson.JSONObject;
import com.nbicc.gywlw.Service.UserService;
import com.nbicc.gywlw.ExceptionHandler.MyException;
import com.nbicc.gywlw.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * Created by fangdong on 2016/11/17.
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;

    //注册
    @RequestMapping(path = {"/reg"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject reg(@RequestParam("user_name") String username,
                          @RequestParam("password") String password,
                          @RequestParam("phone") String phone,
                          @RequestParam(value = "sms_code",defaultValue = "0") String smsCode,  //unfinished
                          @RequestParam("company_name") String companyName,
                          HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.register(username, password, phone, companyName,smsCode);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                response.addCookie(cookie);
                return MyUtil.response(0, "注册成功");
            } else {
                return MyUtil.response(1, map);
            }

        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return MyUtil.response(1, "注册异常");
        }
    }

    //发送短信验证码
    @RequestMapping(path = {"/sendsms"},method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject sendSms(@RequestParam(value = "phone") String phone){
        try{
            if(phone.length() != 11){
                return MyUtil.response(1,"手机号不正确");
            }else{
                userService.sendSms(phone);
                return MyUtil.response(0,"OK!");
            }
        }catch (Exception e){
            logger.error("发送验证码失败" + e.getMessage());
            return MyUtil.response(1, "发送验证码失败");
        }
    }

    //登录
    @RequestMapping(path = {"/login"}, method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public JSONObject login(@RequestParam(value = "phone") String phone,
                        @RequestParam(value = "password") String password,
                        @RequestParam(value = "user_type", defaultValue = "0")String userType,  //默认以普通用户登录0
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(phone, password,Integer.parseInt(userType));
            if( map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                cookie.setMaxAge(3600*24*5);
                response.addCookie(cookie);
                return MyUtil.response(0, "成功");
            } else {
                return MyUtil.response(1, map);
            }

        }catch (Exception e){
            logger.error("登录异常 " + e.getMessage());
            return MyUtil.response(1, "登录异常");
        }
    }
    //忘记密码
    @RequestMapping(path = {"/forgetpsd"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject forgetPsd(@RequestParam("phone")String phone,
                                @RequestParam("sms_code")String smsCode,
                                @RequestParam("new_password")String newPassword){
        try{
            int resultCode = userService.changePsd(phone,smsCode,newPassword,0);
            if(resultCode == -1){
                return MyUtil.response(1,"验证码错误");
            }
            return MyUtil.response(0,"修改密码成功");
        }catch (Exception e){
            logger.error("修改密码异常 " + e.getMessage());
            return MyUtil.response(1, "修改密码异常");
        }
    }




    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) throws MyException {
        userService.logout(ticket);
//        return "redirect:/error";  //返回登录界面
        throw new MyException("请重新登录");
    }

}
