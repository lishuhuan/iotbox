package com.nbicc.gywlw.API;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.gywlw.Service.ApiService;
import com.nbicc.gywlw.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by BigMao on 2017/1/10.
 */
@Controller
@RequestMapping("/api")
public class BaseInfoAPI {
    private static final Logger logger = LoggerFactory.getLogger(BaseInfoAPI.class);

    @Autowired
    private ApiService apiService;

    //返回该用户下所拥有的盒子的唯一标识码、当前工作网络模式、固件版本号、硬件信息（盒子型号）
    @RequestMapping(path = {"/deviceinfo"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject deviceInfo(@RequestParam("token")String token) {
        try {
            return apiService.deviceInfo(token);
        }catch (Exception e){
            logger.error("操作异常 " + e.getMessage());
            return MyUtil.response(-1, "操作异常");
        }
    }

//    @RequestMapping(path = {"/plcinfo"}, method = {RequestMethod.POST})
//    @ResponseBody
//    public JSONObject plcInfo(@RequestParam("token")String token,
//                              @RequestParam("plc_id") String plcId) {
//        try {
//            return apiService.plcInfo(token,plcId);
//        }catch (Exception e){
//            logger.error("操作异常 " + e.getMessage());
//            return MyUtil.response(-1, "操作异常");
//        }
//    }



}
