package com.nbicc.gywlw.Controller;


import com.nbicc.gywlw.Service.UserService;
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
 * Created by BigMao on 2016/11/17.
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;

    @RequestMapping(path = {"/reg"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(@RequestParam("user_name") String username,
                      @RequestParam("password") String password,
                      @RequestParam("phone") String phone,
                      @RequestParam(value = "sms_code",defaultValue = "0") String smsCode,
                      @RequestParam("company_name") String companyName,
                      HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.register(username, password, phone, companyName);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                response.addCookie(cookie);
                return MyUtil.getJSONString(0, "注册成功");
            } else {
                return MyUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return MyUtil.getJSONString(1, "注册异常");
        }
    }

    @RequestMapping(path = {"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(@RequestParam(value = "phone") String phone,
                        @RequestParam(value = "password") String password,
                        @RequestParam(value = "user_type", defaultValue = "1")int userType,
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(phone, password);
            if( map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                cookie.setMaxAge(3600*24*5);
                response.addCookie(cookie);
                return MyUtil.getJSONString(0, "成功");
            } else {
                return MyUtil.getJSONString(1, map);
            }

        }catch (Exception e){
            logger.error("登录异常 " + e.getMessage());
            return MyUtil.getJSONString(1, "登录异常");
        }
    }

    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";  //返回首页
    }

}
