package com.nbicc.gywlw.API;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.nbicc.gywlw.Model.GywlwHistoryItem;
import com.nbicc.gywlw.Model.GywlwRegInfo;
import com.nbicc.gywlw.Service.ApiService;
import com.nbicc.gywlw.Service.ManufacturerService;
import com.nbicc.gywlw.Service.ProjectService;
import com.nbicc.gywlw.mapper.GywlwRegInfoMapper;
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
 * 对外api接口，详细需求和api文档查看 《工业物联网盒子API接口需求(讨论后稿).docx》和《工业物联网盒子对外接口1.1.docx》
 */
@Controller
@RequestMapping("/api")
public class BaseInfoAPI {
    private static final Logger logger = LoggerFactory.getLogger(BaseInfoAPI.class);

    @Autowired
    private ApiService apiService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private GywlwRegInfoMapper gywlwRegInfoMapper;

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

    @RequestMapping(path = {"/plcinfo"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject plcInfo(@RequestParam("token")String token,
                              @RequestParam("device_id") String deviceId) {
        try {
            return apiService.plcInfo(token,deviceId);
        }catch (Exception e){
            logger.error("操作异常 " + e.getMessage());
            return MyUtil.response(-1, "操作异常");
        }
    }

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
            return apiService.uploadOrderInfo(token,deviceId,orderCode,orderNum);
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
                                        @RequestParam(value = "end_time",defaultValue = "4640396560000")String endTime,
                                        @RequestParam(value = "limit",defaultValue = "100")Integer limit,
                                        @RequestParam(value = "cursor",defaultValue = "0")Integer cursor
                                        ){
        try {

            GywlwRegInfo regInfo = gywlwRegInfoMapper.selectByPrimaryKey(regId);
            if(regInfo == null){
                return MyUtil.response(0,"查不到该reg_id");
            }
            JSONObject result = apiService.checkAdminAndDevice(token,regInfo.getDeviceId());
            if(result != null){
                return result;
            }
            //todo
            PageInfo<GywlwHistoryItem> list = projectService.getHistoryDataForReg(regId,startTime,endTime,cursor,limit);

            return MyUtil.response(0, list);
        } catch (ParseException e) {
            logger.error("查询数据项历史数据出错" + e.getMessage());
            return MyUtil.response(1, "查询数据项历史数据出错!");
        }
    }

    @RequestMapping(path = {"/rules"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject rules(@RequestParam("token") String token,
                            @RequestParam("device_id") String deviceId){
        try{
            return apiService.getRules(token,deviceId);
        } catch (Exception e) {
            logger.error("查询规则数据出错" + e.getMessage());
            return MyUtil.response(1, "查询规则数据出错!");
        }
    }

    @RequestMapping(path = { "/devicealarmlist" }, method = { RequestMethod.POST })
    @ResponseBody
    public JSONObject deviceAlarmlist(@RequestParam("token") String token,
                                      @RequestParam(value = "start_time",defaultValue = "0") String startTime,
                                      @RequestParam(value = "end_time",defaultValue = "2480036920") String endTime,
                                      @RequestParam(value = "device_id") String deviceId,
                                      @RequestParam(value = "severity",defaultValue = "ALL")String severity) {
        try{
            JSONObject result = apiService.checkAdminAndDevice(token,deviceId);
            if(result == null) {
                List<GywlwHistoryItem> list = manufacturerService.getDeviceAlarmlist(startTime, endTime, deviceId, severity);
                return MyUtil.response(0, list);
            }
            return result;
        }catch (Exception e){
            logger.error("获取plc告警失败" + e.getMessage());
            return MyUtil.response(1, "获取plc告警失败");
        }
    }
    
    

    @RequestMapping(path = { "/totalproduct" }, method = { RequestMethod.POST })
    @ResponseBody
    public JSONObject deviceTotalOrder(@RequestParam("token") String token,
    								   @RequestParam(value = "device_id") String deviceId) {
        try{
        	JSONObject result = apiService.checkAdminAndDevice(token,deviceId);
        	if(result == null) {
        		JSONObject totel = apiService.getDeviceTotalOrder(deviceId);
                return totel;
            }
        	else{
        		return result;
        	}
        }catch (Exception e){
            logger.error("获取盒子总产量失败" + e.getMessage());
            return MyUtil.response(1, "获取盒子总产量失败");
        }
    }

}
