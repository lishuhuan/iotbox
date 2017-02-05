package com.nbicc.gywlw.Controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.gywlw.Service.ApiService;
import com.nbicc.gywlw.Service.RefreshService;
import com.nbicc.gywlw.util.InitDataForHistory;
import com.nbicc.gywlw.util.MyUtil;
import com.nbicc.gywlw.util.RedisAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    @Autowired
    private RefreshService refreshService;
    @Autowired
    InitDataForHistory initDataForHistory;
    @Autowired
    ApiService apiService;
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

    //数据刷新
    @RequestMapping(path = {"/refresh"}, method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String refreshData(){
        try {
            refreshService.refresh();
            return "ok";
        }catch (Exception e){
            logger.error("refresh失败" + e.getMessage());
            return "refresh失败";
        }
    }

    //测试事务
    @RequestMapping(path = {"/testtransaction"}, method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String testTransaction(){
        try {
            apiService.testTransaction();
            return "ok";
        }catch (Exception e){
            logger.error("exception" + e.getMessage());
            return "exception";
        }
    }
}
