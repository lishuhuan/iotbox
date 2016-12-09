package com.nbicc.gywlw.Controller;

import com.nbicc.gywlw.util.InitDataForHistory;
import com.nbicc.gywlw.util.MyUtil;
import com.nbicc.gywlw.util.RedisAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by BigMao on 2016/12/1.
 */
@Controller
public class TestController {
    @Autowired
    InitDataForHistory initDataForHistory;
    @RequestMapping(path = {"/insertdata"}, method = {RequestMethod.POST})
    @ResponseBody
    public String insertdata(){
        initDataForHistory.add();
        return MyUtil.getJSONString(0,"ok");
    }

    @RequestMapping(path = {"/testredis"}, method = {RequestMethod.POST})
    @ResponseBody
    public String testRedis(){
        RedisAPI redisAPI = new RedisAPI();
        redisAPI.set("aaa","aab");
        String str = redisAPI.get("aaa");
        return MyUtil.getJSONString(0,str);
    }
}
