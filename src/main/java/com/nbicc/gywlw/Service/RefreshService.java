package com.nbicc.gywlw.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nbicc.gywlw.Model.GpioDataModel;
import com.nbicc.gywlw.Model.GywlwDevice;
import com.nbicc.gywlw.mapper.GywlwDeviceMapper;
import com.nbicc.gywlw.mapper.GywlwHistoryItemMapper;
import com.nbicc.gywlw.mapper.GywlwPlcInfoMapper;
import com.nbicc.gywlw.mapper.GywlwRegInfoMapper;
import com.nbicc.gywlw.util.HttpClientUtil;
import com.nbicc.gywlw.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by BigMao on 2016/12/23.
 */
@Service
public class RefreshService {
    private static final Logger logger = LoggerFactory.getLogger(RefreshService.class);
    @Autowired
    private GywlwDeviceMapper gywlwDeviceMapper;
    @Autowired
    private GywlwPlcInfoMapper gywlwPlcInfoMapper;
    @Autowired
    private GywlwHistoryItemMapper gywlwHistoryItemMapper;
    @Autowired
    private GywlwRegInfoMapper gywlwRegInfoMapper;

    public void refresh(){
        refreshData();
    }

    public void refreshData() {
        logger.info("同步gpio数据准备工作： " + new Date());
        List<GywlwDevice> devices = gywlwDeviceMapper.selectAll();
        if(devices.size() != 0){
            for (GywlwDevice device : devices) {
                String gpioId = device.getGpioId();
                if(gpioId == null){
                    continue;
                }
                Long timestamp = 1482715588272L; //20161226092624
                List<String> requestList = new ArrayList<>();
                List<String> gpioList = new ArrayList<>();
                gpioList.add("gpio_1");
                gpioList.add("gpio_7");
                requestList.addAll(gpioList);
                requestList.add("time");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deviceId",gpioId);
                jsonObject.put("idList",requestList);
                jsonObject.put("timestamp",timestamp);
                System.out.println("param: " + jsonObject);

                logger.info("准备工作结束，发送psot请求： " + new Date());

                String str = HttpClientUtil.post(jsonObject);
                //handle response
                JSONObject json = new JSONObject();
                Map<String, Object> map1 = JSON.parseObject(str);
                LinkedList<Map> linkedList = new LinkedList<>();
                List<Map> list1 = JSON.parseArray(map1.get("result_data").toString(), Map.class);
                System.out.println("response data:"+ MyUtil.response(0,list1));
                logger.info("请求成功，开始处理数据： " + new Date());
//                if(list1.size() != 0){
//                    handler(list1,device,gpioList);
//                }
            }
        }
    }

    public void refreshRules(){

    }

    private void handler(List<Map> list, GywlwDevice device, List<String> gpioList){
        GpioDataModel dataModel = new GpioDataModel();
        List<GpioDataModel> gpioDataList = new ArrayList<>();

        for(Map map : list){
            dataModel.setTime((String) map.get("time"));
            dataModel.setTimestamp(String.valueOf(map.get("timestamp")));
            for(String gpio : gpioList){
//                if(map.get(gpio)!=null){
//                    List<Map> maps = (List<Map>) map.get("gpio");
//                    Gpio gpio1 = new Gpio();
//                    gpio1.setValue();
//                    gpio1.setAlarm()
//                    dataModel.setGpio();
//                }
            }
        }
    }





}
