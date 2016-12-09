package com.nbicc.gywlw.Controller;

import com.alibaba.fastjson.JSONObject;
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
    public JSONObject insertdata(){
        initDataForHistory.add();
        return MyUtil.response(0,"ok");
    }

    @RequestMapping(path = {"/testredis"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject testRedis(){
        RedisAPI redisAPI = new RedisAPI();
        redisAPI.set("aaa","aab");
        String str = redisAPI.get("aaa");
        return MyUtil.response(0,str);
    }
}
