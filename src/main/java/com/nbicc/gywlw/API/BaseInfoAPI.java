package com.nbicc.gywlw.API;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.gywlw.Model.GywlwHistoryItem;
import com.nbicc.gywlw.Service.ApiService;
import com.nbicc.gywlw.Service.ProjectService;
import com.nbicc.gywlw.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.List;

/**
 * Created by BigMao on 2017/1/10.
 */
@Controller
@RequestMapping("/api")
public class BaseInfoAPI {
    private static final Logger logger = LoggerFactory.getLogger(BaseInfoAPI.class);

    @Autowired
    private ApiService apiService;
    @Autowired
    private ProjectService projectService;

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
    @RequestMapping(path = {"/devicestatus"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject deviceStatus(@RequestParam("token")String token,
                                   @RequestParam(value = "device_id", required = false) String deviceId){
        try {
            return apiService.deviceStatus(token,deviceId);
        }catch (Exception e){
            logger.error("操作异常 " + e.getMessage());
            return MyUtil.response(-1, "操作异常");
        }
    }

    @RequestMapping(path = {"/uploadorderinfo"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject uploadOrderInfo(@RequestParam("token")String token,
                                      @RequestParam(value = "device_id") String deviceId,
                                      @RequestParam("order_code")String orderCode,
                                      @RequestParam("order_num")String orderNum){
        try {
            String str = apiService.uploadOrderInfo(token,deviceId,orderCode,orderNum);
            if("ok".equals(str)) {
                return MyUtil.response(0, "上传订单信息成功！");
            }else{
                return MyUtil.response(1,str);
            }
        }catch (Exception e){
            logger.error("上传订单信息异常 " + e.getMessage());
            return MyUtil.response(-1, "上传订单信息异常");
        }
    }

    @RequestMapping(path = {"/reginfo"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject regInfo(@RequestParam("token")String token,
                              @RequestParam(value = "device_id") String deviceId){
        try {
            return apiService.regInfo(token,deviceId);
        }catch (Exception e){
            logger.error("查询reginfo异常 " + e.getMessage());
            return MyUtil.response(-1, "查询reginfo异常");
        }
    }

    @RequestMapping(path = {"/historydataforreg"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject historyDataForReg(@RequestParam("token")String token,
                                        @RequestParam("reg_id")String regId,
                                        @RequestParam(value = "start_time",defaultValue = "0")String startTime,
                                        @RequestParam(value = "end_time",defaultValue = "4640396560000")String endTime
                                        ){
        try {
            //todo token检查
            List<GywlwHistoryItem> list = projectService.getHistoryDataForReg(regId,startTime,endTime);
            return MyUtil.response(0, list);
        } catch (ParseException e) {
            logger.error("查询数据项历史数据出错" + e.getMessage());
            return MyUtil.response(1, "查询数据项历史数据出错!");
        }
    }

}
