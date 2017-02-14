package com.nbicc.gywlw.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nbicc.gywlw.Model.*;
import com.nbicc.gywlw.mapper.*;
import com.nbicc.gywlw.util.HttpsTest;
import com.nbicc.gywlw.util.MyUtil;
import com.nbicc.gywlw.util.RedisAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Created by BigMao on 2016/12/23.
 */
@Service
public class RefreshService {
    private static final String[] ALARM = {"alarm1","alarm2"};

    private static final Logger logger = LoggerFactory.getLogger(RefreshService.class);
    @Autowired
    private GywlwDeviceMapper gywlwDeviceMapper;
    @Autowired
    private GywlwPlcInfoMapper gywlwPlcInfoMapper;
    @Autowired
    private GywlwHistoryItemMapper gywlwHistoryItemMapper;
    @Autowired
    private GywlwRegInfoMapper gywlwRegInfoMapper;
    @Autowired
    private GywlwHistoryDataForGPIOMapper gywlwHistoryDataForGPIOMapper;
    @Autowired
    private GywlwProjectDeviceGroupMapper gywlwProjectDeviceGroupMapper;
    @Autowired
    private GywlwWarningRulesMapper gywlwWarningRulesMapper;
    @Autowired
    private GywlwDeviceGpioMapper gywlwDeviceGpioMapper;
    @Autowired
    private GywlwBrandMapper gywlwBrandMapper;
    @Autowired
    private GywlwDeviceOrderMapper gywlwDeviceOrderMapper;


    public void refresh(){
        refreshConfigParams();
        refreshParamsForPlc();  //同步plc数据项设置
        refreshRulesForPlc();   //同步plc rules
        refreshParamsForGpio();    //同步gpio参数设置
        refreshRulesForGpio();    //同步gpio rules
        refreshDataForGpio();
        refreshDataForPlc();
    }

    //PLC，默认一个物联网盒子上连接的plc都是同一型号(目前是一对一)
    public void refreshDataForPlc(){
        long a = System.currentTimeMillis();
        JedisPool pool = RedisAPI.getPool();
        Jedis jedis = pool.getResource();
        List<GywlwDevice> devices = gywlwDeviceMapper.selectAll();
        if(devices.size() != 0) {
            for (GywlwDevice device : devices) {
                List<String> deviceIdList = new ArrayList<>();
                String sdkKey = "";
                Long timestamp = 0L;
                String redisKey = null;
//                Long timestamp = 1482798633000L;//test
//                timestamp = device.getLastConnected().getTime(); //当前时间减1天
                List<String> regList = new ArrayList<>();
                List<String> requestList = new ArrayList<>();
                List<GywlwPlcInfo> plcinfos = gywlwPlcInfoMapper.selectByDeviceId(device.getDeviceId());
                if (plcinfos.size() != 0) {
                    for (GywlwPlcInfo plcInfo : plcinfos) {
                        if(plcInfo.getSubdeviceId() == null){
                            continue;
                        }
                        logger.info("同步plc数据准备工作： " + new Date());
                        //redis保存最近一次的更新时间
                        redisKey = plcInfo.getSubdeviceId();
                        if(jedis.get(redisKey)!=null){
                            timestamp = Long.parseLong(jedis.get(plcInfo.getSubdeviceId()));
                        }else{
                            GywlwHistoryItem historyItem = gywlwHistoryItemMapper.getLastTimeByPlcId(plcInfo.getId());
                            if(historyItem == null){
                                timestamp = System.currentTimeMillis() - 86400L; //当前时间减1天
                            }else{
                                timestamp = historyItem.getItemTime().getTime();
                            }
                        }
                        deviceIdList.add(plcInfo.getSubdeviceId());
                        sdkKey = plcInfo.getContent(); //content保存着sdkkey
                        List<GywlwRegInfo> reglists = gywlwRegInfoMapper.selectByPlcId(plcInfo.getId());
                        if(reglists.size() != 0){
                            for(GywlwRegInfo reg: reglists){
                                regList.add(reg.getRegAddress());
                            }
                        }
                    }
                }else{
                    continue;
                }
                if(regList != null) {
                    requestList.addAll(regList);
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deviceIdList",deviceIdList);
                jsonObject.put("sdkKey",sdkKey);
                jsonObject.put("timestamp",timestamp);
                jsonObject.put("idList",requestList);

//                System.out.println("请求参数: " + jsonObject);
//                logger.info("准备工作结束，发送psot请求： " + new Date());
                String str = null;
                try {
                    str = HttpsTest.postForm(jsonObject,2);  //2代表plc通道，也就是走子设备通道
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(str != null && str.length() > 1000) {
                    logger.info("plc待同步数据 : " + str.substring(0,1000));
                }else{
                    logger.info("plc待同步数据 : " + str);
                }
                //handle response
                if(str == null || str.equals("0")){
                    continue;
                }
//                JSONObject json = new JSONObject();
                Map<String, Object> map1 = JSON.parseObject(str);
                List<Map> list1 = JSON.parseArray(map1.get("result_data").toString(), Map.class);
   //             System.out.println("list1.size():"+list1.toString());
//                logger.info("请求成功，开始处理数据： " + new Date());
                if(list1.size() > 0){
                    try {
                        jedis.set(redisKey, list1.get(0).get("timestamp").toString());
                        //单线程
//                        handlerForPlc(list1,regList);
                        //多线程
                        LinkedList<Map> linkedList = new LinkedList<>();
                        linkedList.addAll(list1);
                        handleByThread(linkedList,regList,new GywlwDevice(),0,15);  //0表示handle plc
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("结束*****************************");
        long b = System.currentTimeMillis();
        System.out.println("plc数据同步结束，总花费时间:" + (b - a) + "毫秒***********************");
    }

    //GPIO
    public void refreshDataForGpio() {
        long a = System.currentTimeMillis();
        List<GywlwDevice> devices = gywlwDeviceMapper.selectAll();
        if(devices.size() != 0){
            for (GywlwDevice device : devices) {
                String gpioId = device.getGpioId();
                if(gpioId == null){
                    continue;
                }
                logger.info("同步gpio数据准备工作:..........");
                Long timestamp;
                JedisPool pool = RedisAPI.getPool();
                Jedis jedis = pool.getResource();
                if(jedis.get(device.getGpioId())!=null){
                    timestamp = Long.parseLong(jedis.get(device.getGpioId()));
                }else{
                    GywlwHistoryDataForGPIO dataForGPIO = gywlwHistoryDataForGPIOMapper.
                            getLastTimeByDeviceId(device.getDeviceId());
                    if(dataForGPIO == null){
                        timestamp = System.currentTimeMillis() - 86400L; //当前时间减1天
                    }else{
                        timestamp = dataForGPIO.getItemTime().getTime();
                    }
                }
                List<String> gpioList = new ArrayList<>();
                gpioList.add("gpio_1");
                gpioList.add("gpio_2");
                gpioList.add("gpio_3");
                gpioList.add("gpio_4");
                gpioList.add("gpio_5");
                gpioList.add("gpio_6");
                gpioList.add("gpio_7");
                gpioList.add("gpio_8");
                gpioList.add("throughput");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deviceId",gpioId);
                jsonObject.put("idList",gpioList);
                jsonObject.put("timestamp",timestamp);
//                System.out.println("请求参数: " + jsonObject);
//                logger.info("准备工作结束，发送psot请求： " + new Date());

                String str = null;
                try {
                    str = HttpsTest.postForm(jsonObject,1); //1代表走设备通道
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(str != null && str.length() > 1000) {
                    logger.info("gpio待同步数据 : " + str.substring(0,1000));
                }else{
                    logger.info("gpio待同步数据 : " + str);
                }

                //handle response
                if(str == null || str.equals("0")){
                    continue;
                }
//                JSONObject json = new JSONObject();
                Map<String, Object> map1 = JSON.parseObject(str);
                List<GpioDataModel> list1 = JSON.parseArray(map1.get("result_data").toString(), GpioDataModel.class);
//                System.out.println("response data:"+ list1);
//                logger.info("请求成功，开始处理数据： " + new Date());
                if(list1.size() != 0){
                    //储存该设备最新一次更新的时间戳
                    jedis.set(device.getGpioId(),list1.get(0).getTimestamp());
                    //单线程
//                    handler(list1,device,gpioList);
                    //多线程
                    LinkedList<GpioDataModel> linkedList = new LinkedList<>();
                    linkedList.addAll(list1);
                    try {
                        saveThroughPut(list1,device);  //存储累计产量
                        handleByThread(linkedList,gpioList,device,1,15);  //1表示handle gpio data
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("结束*****************************");
        long b = System.currentTimeMillis();
        System.out.println("gpio数据同步结束，总花费时间:" + (b - a) + "毫秒***********************");
    }

    //存储累计产量
    private void saveThroughPut(List<GpioDataModel> list1, GywlwDevice device) {
        for(GpioDataModel model : list1){
            if(model.getThroughPut() != null){
                Integer throughPut = model.getThroughPut().getValue();
                GywlwDeviceOrder gywlwDeviceOrder = gywlwDeviceOrderMapper.selectByDeviceId(device.getDeviceId());
                gywlwDeviceOrder.setOrderFinished(throughPut);
                gywlwDeviceOrderMapper.updateByPrimaryKeySelective(gywlwDeviceOrder);
                break;
            }
        }
    }

    //同步gpio rules
    public void refreshRulesForGpio(){
        refreshParams(3);
    }

    //同步plc rules
    public void refreshRulesForPlc(){
        refreshParams(1);
    }

    public void refreshConfigParams(){
        List<GywlwDevice> devices = gywlwDeviceMapper.selectAll();
        if(devices.size() != 0) {
            for (GywlwDevice device : devices) {
                String gpioId = device.getGpioId();
                if (gpioId == null) {
                    continue;
                }
                logger.info("同步设备配置(ConfigParams)的准备工作.....................");
                Long timestamp = device.getLastConnected().getTime();
                List<String> requestList = new ArrayList<>();
                requestList.add("hardware_edition");
                requestList.add("software_edition");
                requestList.add("3g_mode");
                requestList.add("serial0_mode"); //每个通道对应设备的驱动类型；
                requestList.add("serial1_mode");
                requestList.add("serial2_mode");
                requestList.add("serial3_mode");
                requestList.add("serial4_mode");
                requestList.add("serial0_subdev_id");
                requestList.add("serial1_subdev_id");
                requestList.add("serial2_subdev_id");
                requestList.add("serial3_subdev_id");
                requestList.add("serial4_subdev_id");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deviceId",gpioId);
                jsonObject.put("idList",requestList);
                jsonObject.put("timestamp",timestamp);
                System.out.println("请求参数: " + jsonObject);
//                logger.info("准备工作结束，发送psot请求： " + new Date());
                String str = null;
                try {
                    str = HttpsTest.postForm(jsonObject,1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(str == null || str.equals("0")){
                    continue;
                }
                if(str.length() > 1000) {
                    logger.info("同步设备配置(ConfigParams)的待同步数据 : " + str.substring(0,1000));
                }else{
                    logger.info("同步设备配置(ConfigParams)的待同步数据 : " + str);
                }
                //handle response
//                JSONObject json = new JSONObject();
                Map<String, Object> map1 = JSON.parseObject(str);
                List<Map> list1 = JSON.parseArray(map1.get("result_data").toString(), Map.class);
//                System.out.println("返回数据:  "+list1.toString());
//                logger.info("请求成功，开始处理数据： " + new Date());
                if(list1.size() > 0){
                    try {
                        handlerForConfigParams(list1,device);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    //同步plc数据项设置
    public void refreshParamsForPlc(){
        refreshParams(0);
    }

    //同步gpio参数设置
    public void refreshParamsForGpio(){
        refreshParams(2);
    }


    //mark=0表示同步plc数据项设置，mark=1表示同步plcrules数据；
    public void refreshParams(int mark){
        List<GywlwDevice> devices = gywlwDeviceMapper.selectAll();
        if(devices.size() != 0) {
            for (GywlwDevice device : devices) {
                String gpioId = device.getGpioId();
                if (gpioId == null) {
                    continue;
                }
                Long timestamp;
                timestamp = device.getLastConnected().getTime();
                List<String> requestList = new ArrayList<>();
                String requestData = null;
                if(mark == 0) {
                    requestData = "field_data"; //plc params;
                    logger.info("同步plc配置信息的准备工作.....................");
                }else if(mark == 1){
                    requestData = "rules_data"; //plc rules;
                    logger.info("同步plc规则的准备工作.....................");
                }else if(mark == 2){
                    requestData = "field_data_gpio"; //gpio params
                    logger.info("同步gpio配置信息的准备工作.....................");
                }else if(mark == 3){
                    requestData = "rules_data_gpio";  //gpio rules
                    logger.info("同步gpio规则的准备工作.....................");
                }
                requestList.add(requestData);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deviceId",gpioId);
                jsonObject.put("idList",requestList);
                jsonObject.put("timestamp",timestamp);
//                System.out.println("请求参数: " + jsonObject);
//                logger.info("准备工作结束，发送psot请求： " + new Date());
                String str = null;
                try {
                    str = HttpsTest.postForm(jsonObject,1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(str == null || str.equals("0")){
                    continue;
                }
                if(str.length() > 1000) {
                    logger.info("Params待同步数据 : " + str.substring(0,1000));
                }else{
                    logger.info("Params待同步数据 : " + str);
                }
                //handle response
//                JSONObject json = new JSONObject();
                Map<String, Object> map1 = JSON.parseObject(str);
                List<Map> list1 = JSON.parseArray(map1.get("result_data").toString(), Map.class);
//                System.out.println("返回数据:  "+list1.toString());
//                logger.info("请求成功，开始处理数据： " + new Date());
                if(list1.size() > 0) {
//                    logger.info("开始解析：-----");
                        String[] strList = null;
                        for (Map map : list1) {
                            if(map.get(requestData) == null){
                                continue;
                            }
                            strList = map.get(requestData).toString().split(",");
                            break;
                        }
//                    System.out.println(requestData + "   " + strList[0] + "   " + strList[1]);
                        if (strList != null && !"".equals(strList[0])) {
                            if (mark == 0) {
                                handlerForPlcParams(strList, device);
                            } else if (mark == 1) {
                                handlerForPlcRules(strList, device);
                            } else if (mark == 2) {
                                handlerForGpioParams(strList, device);
                            } else if (mark == 3) {
                                handlerForGpioRules(strList, device);
                            }
                        } else {
                            if (mark == 2) {
                                handlerForGpioParams(strList, device); //无论如何都要运行
                            }
                        }

                }
            }
        }
    }

    private void handlerForGpioRules(String[] strList, GywlwDevice device) {
        int isFirstOne = 0;
        for(String str : strList){
            String gpioId = device.getGpioId();
            Long timestamp = device.getLastConnected().getTime();
            List<String> requestList = new ArrayList<>();
            requestList.add(str);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceId",gpioId);
            jsonObject.put("idList",requestList);
            jsonObject.put("timestamp",timestamp);
            System.out.println("请求参数: " + jsonObject);
//            logger.info("handlerForPlcParams准备工作结束，发送psot请求： " + new Date());

            String responseStr = null;
            try {
                responseStr = HttpsTest.postForm(jsonObject,1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(responseStr == null || responseStr.equals("0")){
                continue;
            }
//            System.out.println("待同步数据 : "+ responseStr);
            //handle response
//            JSONObject json = new JSONObject();
            Map<String, Object> map1 = JSON.parseObject(responseStr);
            List<Map> map2 = JSON.parseArray(map1.get("result_data").toString(),Map.class);
            GpioRulesModel model = JSON.parseObject(map2.get(0).get(str).toString(),GpioRulesModel.class);

//            logger.info("请求成功，开始处理数据： " + new Date());
            //更新rules
            GywlwWarningRules gywlwWarningRules = new GywlwWarningRules();
            gywlwWarningRules.setDeviceId(device.getDeviceId());
            gywlwWarningRules.setRuleName(model.getRules_name());
            gywlwWarningRules.setRuleName2(model.getRules_name2());
            gywlwWarningRules.setRuleField(model.getRules_field());
            if(model.getRules_condition() != null){
                gywlwWarningRules.setRuleCondition(Integer.parseInt(model.getRules_condition()));
            }
            if(model.getRules_id() != null){
                gywlwWarningRules.setRuleId(Integer.parseInt(model.getRules_id()));
            }
            if(model.getRules_alarmlevel() != null) {
                gywlwWarningRules.setRuleAlarmlevel(Integer.parseInt(model.getRules_alarmlevel()));
            }

//            logger.info("更新数据库...");
            //先删除已经存在的规则，再插入新规则
            if(gywlwWarningRules.getRuleId()!=null) {
                gywlwWarningRulesMapper.deleteByPrimaryKey(gywlwWarningRules.getRuleId());
                gywlwWarningRulesMapper.insertSelective(gywlwWarningRules);
            }
        }
    }

    private void handlerForGpioParams(String[] strList, GywlwDevice device) {
        List<GywlwDeviceGpio> list2 = gywlwDeviceGpioMapper.selectByDeviceId(device.getDeviceId());
        if(list2.size() == 0){
            //若是新设备，则进行初始化，增加8个gpio信息
            String[] GpioList = {"gpio_1","gpio_2","gpio_3","gpio_4","gpio_5","gpio_6","gpio_7","gpio_8"};
            List<String> listOfGpio = Arrays.asList(GpioList);
            for(String str : listOfGpio){
                GywlwDeviceGpio gywlwDeviceGpio = new GywlwDeviceGpio();
                gywlwDeviceGpio.setId(UUID.randomUUID().toString().replace("-",""));
                gywlwDeviceGpio.setFieldAddress(str);
                gywlwDeviceGpio.setDeviceId(device.getDeviceId());
                gywlwDeviceGpioMapper.insertSelective(gywlwDeviceGpio);
            }
        }
        for(String str : strList){
            String gpioId = device.getGpioId();
            Long timestamp = device.getLastConnected().getTime();
            List<String> requestList = new ArrayList<>();
            requestList.add(str);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceId",gpioId);
            jsonObject.put("idList",requestList);
            jsonObject.put("timestamp",timestamp);
            System.out.println("请求参数: " + jsonObject);
//            logger.info("handlerForPlcParams准备工作结束，发送psot请求： " + new Date());

            String responseStr = null;
            try {
                responseStr = HttpsTest.postForm(jsonObject,1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(responseStr == null || responseStr.equals("0")){
                continue;
            }
//            System.out.println("待同步数据 : "+ responseStr);
            //handle response
//            JSONObject json = new JSONObject();
            Map<String, Object> map1 = JSON.parseObject(responseStr);
            List<Map> map2 = JSON.parseArray(map1.get("result_data").toString(),Map.class);
            if(map2.size() == 0){
                continue;
            }
            GpioParamsModel model;
            model = JSON.parseObject(map2.get(map2.size()-1).get(str).toString(),GpioParamsModel.class);
//                System.out.println("response data:"+ list1);
//            logger.info("请求成功，开始处理数据： " + new Date());
            //更新gpio params
            GywlwDeviceGpio gywlwDeviceGpio = gywlwDeviceGpioMapper.selectByDeviceIdAndGpioId(device.getDeviceId(),
                    model.getField_address().substring(0,6));
            gywlwDeviceGpio.setFieldAttach(model.getField_attach());
            gywlwDeviceGpio.setFieldFunctioncode(model.getField_functioncode());
            gywlwDeviceGpio.setFieldName(model.getField_name());
            gywlwDeviceGpio.setFieldName2(model.getField_name2());
            if(model.getField_rw() != null) {
                gywlwDeviceGpio.setFieldRw(Integer.parseInt(model.getField_rw()));
            }
//            logger.info("更新数据库...");
            //先删除已经存在的gpio，再插入新gpio
            if(gywlwDeviceGpio.getFieldAddress()!=null) {
                gywlwDeviceGpioMapper.updateByPrimaryKeySelective(gywlwDeviceGpio);
            }
        }
    }

    //todo setPlcId那里目前只实现了一对一这一种情况，日后补充一对多情况
    private void handlerForPlcRules(String[] strList, GywlwDevice device) {
        int isFirstOne = 0;
        for(String str : strList){
            String gpioId = device.getGpioId();
            Long timestamp = device.getLastConnected().getTime();
            List<String> requestList = new ArrayList<>();
            requestList.add(str);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceId",gpioId);
            jsonObject.put("idList",requestList);
            jsonObject.put("timestamp",timestamp);
            System.out.println("请求参数: " + jsonObject);
//            logger.info("handlerForPlcParams准备工作结束，发送psot请求： " + new Date());

            String responseStr = null;
            try {
                responseStr = HttpsTest.postForm(jsonObject,1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(responseStr==null || responseStr.equals("0")){
                continue;
            }
//            System.out.println("待同步数据 : "+ responseStr);
            //handle response
//            JSONObject json = new JSONObject();
            Map<String, Object> map1 = JSON.parseObject(responseStr);
            List<Map> map2 = JSON.parseArray(map1.get("result_data").toString(),Map.class);
            PlcRulesModel model = JSON.parseObject(map2.get(0).get(str).toString(),PlcRulesModel.class);

//                System.out.println("response data:"+ list1);
//            logger.info("请求成功，开始处理数据： " + new Date());
            //更新rules
            GywlwWarningRules gywlwWarningRules = new GywlwWarningRules();
            gywlwWarningRules.setDeviceId(device.getDeviceId());
//            gywlwWarningRules.setPlcId(gywlwPlcInfoMapper.selectByDeviceId1(device.getDeviceId()).getId());
            if(model.getRules_1() != null) {
                gywlwWarningRules.setRule1(Integer.parseInt(model.getRules_1()));
            }
            if(model.getRules_2() != null){
                gywlwWarningRules.setRule2(Integer.parseInt(model.getRules_2()));
            }
            if(model.getRules_alarmlevel() != null){
                gywlwWarningRules.setRuleAlarmlevel(Integer.parseInt(model.getRules_alarmlevel()));
            }
            if(model.getRules_condition() != null){
                gywlwWarningRules.setRuleCondition(Integer.parseInt(model.getRules_condition()));
            }
            if(model.getRules_id() != null){
                gywlwWarningRules.setRuleId(Integer.parseInt(model.getRules_id()));
            }
            if(model.getRules_pass() != null){
                gywlwWarningRules.setRulesPass(Integer.parseInt(model.getRules_pass()));
            }
            gywlwWarningRules.setRuleField(model.getRules_field());
            gywlwWarningRules.setRuleName(model.getRules_name());
            gywlwWarningRules.setRuleName2(model.getRules_name2());

//            logger.info("更新数据库...");
            //先删除已经存在的规则，再插入新规则
            if(gywlwWarningRules.getRuleId()!=null) {
                gywlwWarningRulesMapper.deleteByPrimaryKey(gywlwWarningRules.getRuleId());
                gywlwWarningRulesMapper.insertSelective(gywlwWarningRules);
            }
        }
    }

    //用了硬编码,多线程
    private void handler(List<GpioDataModel> list, GywlwDevice device, List<String> gpioList){
        try {
            List<GywlwHistoryDataForGPIO> listForGpio = new ArrayList<>();
            for (GpioDataModel model : list) {
//            System.out.println(model.getTime());
                badAdd(listForGpio,model,device);
            }
//            logger.info("批量插入数据开始");
            if(listForGpio.size() > 0){
                gywlwHistoryDataForGPIOMapper.insertBatch(listForGpio);
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    //
    public void badAdd(List<GywlwHistoryDataForGPIO> listForGpio, GpioDataModel model,GywlwDevice device) throws ParseException {
        GywlwHistoryDataForGPIO gywlwHistoryDataForGPIO = new GywlwHistoryDataForGPIO();
        //#1
        if(model.getGpio_1() != null) {
            GywlwDeviceGpio gywlwDeviceGpio = gywlwDeviceGpioMapper.selectByDeviceIdAndGpioId(device.getDeviceId(),"gpio_1");
            if(gywlwDeviceGpio != null) {
                gywlwHistoryDataForGPIO.setItemTime(MyUtil.timeTransformToDateNo1000(model.getTimestamp()));
                gywlwHistoryDataForGPIO.setDeviceId(device.getDeviceId());
                gywlwHistoryDataForGPIO.setGpioId(gywlwDeviceGpio.getId());
                gywlwHistoryDataForGPIO.setItemValue(model.getGpio_1().getValue());
                if (model.getGpio_1().getAlarm() != 0) {
                    String alarm = "";
                    if (model.getGpio_1().getAlarm1() != null) {
                        alarm = alarm + model.getGpio_1().getAlarm1();
                    }
                    if (model.getGpio_1().getAlarm2() != null) {
                        alarm = alarm + " " + model.getGpio_1().getAlarm2();
                    }
                    gywlwHistoryDataForGPIO.setAlarm(alarm);
                }
                listForGpio.add(gywlwHistoryDataForGPIO);
            }
        }

        //#2
        if(model.getGpio_2() != null) {
            GywlwDeviceGpio gywlwDeviceGpio = gywlwDeviceGpioMapper.selectByDeviceIdAndGpioId(device.getDeviceId(),"gpio_2");
            if(gywlwDeviceGpio != null) {
                gywlwHistoryDataForGPIO = new GywlwHistoryDataForGPIO();
                gywlwHistoryDataForGPIO.setItemTime(MyUtil.timeTransformToDateNo1000(model.getTimestamp()));
                gywlwHistoryDataForGPIO.setDeviceId(device.getDeviceId());
                gywlwHistoryDataForGPIO.setGpioId(gywlwDeviceGpio.getId());
                gywlwHistoryDataForGPIO.setItemValue(model.getGpio_2().getValue());
                if (model.getGpio_2().getAlarm() != 0) {
                    String alarm = "";
                    if (model.getGpio_2().getAlarm1() != null) {
                        alarm = alarm + model.getGpio_2().getAlarm1();
                    }
                    if (model.getGpio_2().getAlarm2() != null) {
                        alarm = alarm + " " + model.getGpio_2().getAlarm2();
                    }
                    gywlwHistoryDataForGPIO.setAlarm(alarm);
                }
                listForGpio.add(gywlwHistoryDataForGPIO);
            }
        }


        //#3
        if(model.getGpio_3() != null) {
            GywlwDeviceGpio gywlwDeviceGpio = gywlwDeviceGpioMapper.selectByDeviceIdAndGpioId(device.getDeviceId(),"gpio_3");
            if(gywlwDeviceGpio != null) {
                gywlwHistoryDataForGPIO = new GywlwHistoryDataForGPIO();
                gywlwHistoryDataForGPIO.setItemTime(MyUtil.timeTransformToDateNo1000(model.getTimestamp()));
                gywlwHistoryDataForGPIO.setDeviceId(device.getDeviceId());
                gywlwHistoryDataForGPIO.setGpioId(gywlwDeviceGpio.getId());
                gywlwHistoryDataForGPIO.setItemValue(model.getGpio_3().getValue());
                if (model.getGpio_3().getAlarm() != 0) {
                    String alarm = "";
                    if (model.getGpio_3().getAlarm1() != null) {
                        alarm = alarm + model.getGpio_3().getAlarm1();
                    }
                    if (model.getGpio_3().getAlarm2() != null) {
                        alarm = alarm + " " + model.getGpio_3().getAlarm2();
                    }
                    gywlwHistoryDataForGPIO.setAlarm(alarm);
                }
                listForGpio.add(gywlwHistoryDataForGPIO);
            }
        }

        //#4
        if(model.getGpio_4() != null) {
            GywlwDeviceGpio gywlwDeviceGpio = gywlwDeviceGpioMapper.selectByDeviceIdAndGpioId(device.getDeviceId(),"gpio_4");
            if(gywlwDeviceGpio != null) {
                gywlwHistoryDataForGPIO = new GywlwHistoryDataForGPIO();
                gywlwHistoryDataForGPIO.setItemTime(MyUtil.timeTransformToDateNo1000(model.getTimestamp()));
                gywlwHistoryDataForGPIO.setDeviceId(device.getDeviceId());
                gywlwHistoryDataForGPIO.setGpioId(gywlwDeviceGpio.getId());
                gywlwHistoryDataForGPIO.setItemValue(model.getGpio_4().getValue());
                if (model.getGpio_4().getAlarm() != 0) {
                    String alarm = "";
                    if (model.getGpio_4().getAlarm1() != null) {
                        alarm = alarm + model.getGpio_4().getAlarm1();
                    }
                    if (model.getGpio_4().getAlarm2() != null) {
                        alarm = alarm + " " + model.getGpio_4().getAlarm2();
                    }
                    gywlwHistoryDataForGPIO.setAlarm(alarm);
                }
                listForGpio.add(gywlwHistoryDataForGPIO);
            }
        }

        //#5
        if(model.getGpio_5() != null) {
            GywlwDeviceGpio gywlwDeviceGpio = gywlwDeviceGpioMapper.selectByDeviceIdAndGpioId(device.getDeviceId(),"gpio_5");
            if(gywlwDeviceGpio != null) {
                gywlwHistoryDataForGPIO = new GywlwHistoryDataForGPIO();
                gywlwHistoryDataForGPIO.setItemTime(MyUtil.timeTransformToDateNo1000(model.getTimestamp()));
                gywlwHistoryDataForGPIO.setDeviceId(device.getDeviceId());
                gywlwHistoryDataForGPIO.setGpioId(gywlwDeviceGpio.getId());
                gywlwHistoryDataForGPIO.setItemValue(model.getGpio_5().getValue());
                if (model.getGpio_5().getAlarm() != 0) {
                    String alarm = "";
                    if (model.getGpio_5().getAlarm1() != null) {
                        alarm = alarm + model.getGpio_5().getAlarm1();
                    }
                    if (model.getGpio_5().getAlarm2() != null) {
                        alarm = alarm + " " + model.getGpio_5().getAlarm2();
                    }
                    gywlwHistoryDataForGPIO.setAlarm(alarm);
                }
                listForGpio.add(gywlwHistoryDataForGPIO);
            }
        }

        //#6
        if(model.getGpio_6() != null) {
            GywlwDeviceGpio gywlwDeviceGpio = gywlwDeviceGpioMapper.selectByDeviceIdAndGpioId(device.getDeviceId(),"gpio_6");
            if(gywlwDeviceGpio != null) {
                gywlwHistoryDataForGPIO = new GywlwHistoryDataForGPIO();
                gywlwHistoryDataForGPIO.setItemTime(MyUtil.timeTransformToDateNo1000(model.getTimestamp()));
                gywlwHistoryDataForGPIO.setDeviceId(device.getDeviceId());
                gywlwHistoryDataForGPIO.setGpioId(gywlwDeviceGpio.getId());
                gywlwHistoryDataForGPIO.setItemValue(model.getGpio_6().getValue());
                if (model.getGpio_6().getAlarm() != 0) {
                    String alarm = "";
                    if (model.getGpio_6().getAlarm1() != null) {
                        alarm = alarm + model.getGpio_6().getAlarm1();
                    }
                    if (model.getGpio_6().getAlarm2() != null) {
                        alarm = alarm + " " + model.getGpio_6().getAlarm2();
                    }
                    gywlwHistoryDataForGPIO.setAlarm(alarm);
                }
                listForGpio.add(gywlwHistoryDataForGPIO);
            }
        }

        //#7
        if(model.getGpio_7() != null) {
            GywlwDeviceGpio gywlwDeviceGpio = gywlwDeviceGpioMapper.selectByDeviceIdAndGpioId(device.getDeviceId(),"gpio_7");
            if(gywlwDeviceGpio != null) {
                gywlwHistoryDataForGPIO = new GywlwHistoryDataForGPIO();
                gywlwHistoryDataForGPIO.setItemTime(MyUtil.timeTransformToDateNo1000(model.getTimestamp()));
                gywlwHistoryDataForGPIO.setDeviceId(device.getDeviceId());
                gywlwHistoryDataForGPIO.setItemValue(model.getGpio_7().getValue());
                gywlwHistoryDataForGPIO.setGpioId(gywlwDeviceGpio.getId());
                if (model.getGpio_7().getAlarm() != 0) {
                    String alarm = "";
                    if (model.getGpio_7().getAlarm1() != null) {
                        alarm = alarm + model.getGpio_7().getAlarm1();
                    }
                    if (model.getGpio_7().getAlarm2() != null) {
                        alarm = alarm + " " + model.getGpio_7().getAlarm2();
                    }
                    gywlwHistoryDataForGPIO.setAlarm(alarm);
                }
                listForGpio.add(gywlwHistoryDataForGPIO);
            }
        }

        //#8
        if(model.getGpio_8() != null) {
            GywlwDeviceGpio gywlwDeviceGpio = gywlwDeviceGpioMapper.selectByDeviceIdAndGpioId(device.getDeviceId(),"gpio_8");
            if(gywlwDeviceGpio != null) {
                gywlwHistoryDataForGPIO = new GywlwHistoryDataForGPIO();
                gywlwHistoryDataForGPIO.setItemTime(MyUtil.timeTransformToDateNo1000(model.getTimestamp()));
                gywlwHistoryDataForGPIO.setDeviceId(device.getDeviceId());
                gywlwHistoryDataForGPIO.setItemValue(model.getGpio_8().getValue());
                gywlwHistoryDataForGPIO.setGpioId(gywlwDeviceGpio.getId());
                if (model.getGpio_8().getAlarm() != 0) {
                    String alarm = "";
                    if (model.getGpio_8().getAlarm1() != null) {
                        alarm = alarm + model.getGpio_8().getAlarm1();
                    }
                    if (model.getGpio_8().getAlarm2() != null) {
                        alarm = alarm + " " + model.getGpio_8().getAlarm2();
                    }
                    gywlwHistoryDataForGPIO.setAlarm(alarm);
                }
                listForGpio.add(gywlwHistoryDataForGPIO);
            }
        }

    }

    public void handlerForPlc(List<Map> list1, List<String> regList) throws ParseException, CloneNotSupportedException {
        List<GywlwHistoryItem> historyItemList = new ArrayList<>();
        for(Map map : list1){
//            System.out.println(map.get("device_id"));
//            System.out.println(map.get("Y0002"));
//            Map map1 = (JSONObject) JSON.parse(map.get("Y0002").toString());
//            System.out.println(map1.get("value"));
            GywlwHistoryItem gywlwHistoryItem = new GywlwHistoryItem();
            gywlwHistoryItem.setItemTime(MyUtil.timeTransformToDateNo1000(map.get("timestamp").toString())); //s
            GywlwPlcInfo plcInfo = gywlwPlcInfoMapper.selectBySubDeviceId(map.get("device_id").toString());
            gywlwHistoryItem.setPlcId(plcInfo.getId());
            gywlwHistoryItem.setPlcName(plcInfo.getPlcName());
            gywlwHistoryItem.setDeviceId(plcInfo.getDeviceId());
            GywlwDevice gywlwDevice = gywlwDeviceMapper.selectByDeviceId(plcInfo.getDeviceId());
            gywlwHistoryItem.setDeviceName(gywlwDevice.getDeviceName());
            gywlwHistoryItem.setProjectId(gywlwProjectDeviceGroupMapper.selectByDeviceId(plcInfo.getDeviceId()).getProjectId());
            //jedis

            for(String reg : regList){
                if(map.get(reg) == null){
                    continue;
                }
                gywlwHistoryItem.setItemName(reg);
                GywlwRegInfo regInfo = gywlwRegInfoMapper.selectByRegAddress(reg);
                gywlwHistoryItem.setRegId(regInfo.getRegId());
                gywlwHistoryItem.setItemAddress(reg);
                gywlwHistoryItem.setItemAlias(regInfo.getRegAlias());
                Map map1 = (JSONObject) JSON.parse(map.get(reg).toString());
                gywlwHistoryItem.setItemValue(new Double((Integer)map1.get("value")));
                if((int) map1.get("alarm") == 0){
                    historyItemList.add(gywlwHistoryItem);
                    GywlwHistoryItem gywlwHistoryItem1 = (GywlwHistoryItem) gywlwHistoryItem.clone();
                    gywlwHistoryItem = gywlwHistoryItem1;
                    continue;
                }else{
                    for(int i = 0; i < (int) map1.get("alarm");++i){
                        gywlwHistoryItem.setRuleId((Integer) map1.get(ALARM[i]));
                        gywlwHistoryItem.setSeverity(i);//暂且把alarm1当成1级告警，alarm2当成2级告警
                        historyItemList.add(gywlwHistoryItem);
                        GywlwHistoryItem gywlwHistoryItem1 = (GywlwHistoryItem) gywlwHistoryItem.clone();
                        gywlwHistoryItem = gywlwHistoryItem1;
                    }
                }
            }
        }
    //    System.out.println("待插入数据：" + MyUtil.response(0,historyItemList));
        logger.info("批量插入数据start...");
        if(historyItemList.size() > 0) {
            gywlwHistoryItemMapper.insertBatch(historyItemList);
        }
        logger.info("批量插入数据end !");
    }

    public synchronized void handleByThread(LinkedList list1, List<String> regList, GywlwDevice device, int mark,int threadNum)
                throws InterruptedException, ParseException, CloneNotSupportedException {
        int length = list1.size();
        int t1 = length % threadNum == 0 ? length / threadNum : (length / threadNum + 1);
        CountDownLatch latch = new CountDownLatch(threadNum);// 多少协作
//        long a = System.currentTimeMillis();
        if(length < threadNum * 15){
            if(mark == 0) {
                handlerForPlc(list1, regList);
            }else{
                handler(list1,device,regList);
            }
        } else {
            for (int i = 0; i < threadNum; i++) {
                int end = (i + 1) * t1 ;
                end = end > length ? length : end;
                int start = i * t1;
                if (start <= length) {
                    // 继承thread启动线程
                    // HandleThread thread = new HandleThread("线程[" + (i + 1) +"] ",data, i * tl, end > length ? length : end, latch);
                    // thread.start();
                    // 实现Runnable启动线程
                    logger.info("线程[" + (i + 1) + "] " + "  start: " + start + "   end:  " + end);
                    RunnableThread thread = new RunnableThread("线程[" + (i + 1) + "] ",
                            list1, start, end, latch, regList,device,mark);
                    Thread runable = new Thread(thread);
                    runable.start();
                }else{
                    latch.countDown();
                }
            }
            latch.await();// 等待所有工人完成工作
        }
//        System.out.println("结束*****************************");
//        long b = System.currentTimeMillis();
//        System.out.println("数据同步结束，总花费时间:" + (b - a) + "毫秒***********************");
    }

    class RunnableThread implements Runnable {
        private String threadName;
        private List data;
        private int start;
        private int end;
        private CountDownLatch latch;
        private List<String> regList;
        private GywlwDevice device;
        private int mark;

        public RunnableThread(String threadName, List data, int start, int end, CountDownLatch latch,
                              List<String> regList, GywlwDevice device, int mark) {
            this.threadName = threadName;
            this.data = data;
            this.start = start;
            this.end = end;
            this.latch = latch;
            this.regList = regList;
            this.device = device;
            this.mark = mark;
        }

        public void run() {
            // 这里处理数据
//            logger.info("start: " + start + " ||  end: " + end);
            List subList = data.subList(start, end);  //[start,end)
//            System.out.println(threadName + "--" + data.size() + "--" + start + "--" + end + "--");
            // 单个线程中的数据
            try {
                if(mark == 0){
                    handlerForPlc(subList, regList); //handle plc data
                }else{
                    handler(subList,device,regList); //handle gpio data
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            latch.countDown();// 工人完成工作，计数器减一
        }
    }

    public void handlerForConfigParams(List<Map> list1, GywlwDevice device) throws ParseException {
        GywlwDevice gywlwDevice = new GywlwDevice();
        gywlwDevice.setDeviceId(device.getDeviceId());
        List<GywlwPlcInfo> plcInfoList = new ArrayList<>();
        for(Map map : list1){
            //1.更新盒子信息
            gywlwDevice.setHardwareEdition((String) map.get("hardware_edition"));
            gywlwDevice.setSoftwareEdition((String) map.get("software_edition"));
            gywlwDevice.setThreeGMode(Integer.parseInt((String) map.get("3g_mode")));
            gywlwDevice.setLastConnected(MyUtil.timeTransformToDateNo1000(map.get("timestamp").toString()));
            //2.更新通道对应plc的驱动类型
            Map<Integer,String> brandMap = new HashMap<>();
            brandMap.put(0,"三菱");
            brandMap.put(1,"三菱");
            brandMap.put(2,"松下");
            brandMap.put(3,"台达");
            brandMap.put(4,"西门子");
            Map<Integer,String> typeMap = new HashMap<>();
            typeMap.put(0,"FX系列");
            typeMap.put(1,"FX系列");
            typeMap.put(2,"FP系列");
            typeMap.put(3,"台达");
            typeMap.put(4,"S7-200");
            //#0
            GywlwPlcInfo plcInfo = new GywlwPlcInfo();
            plcInfo.setSubdeviceId((String) map.get("serial0_subdev_id"));
            if(plcInfo.getSubdeviceId()!=null && !plcInfo.getSubdeviceId().equals("0")) {
                if (map.get("serial0_mode") != null) {
                    Integer serialxMode = Integer.parseInt((String) map.get("serial0_mode"));
                    plcInfo.setPlcBrand(brandMap.get(serialxMode));
                    plcInfo.setPlcType(typeMap.get(serialxMode));
                    plcInfo.setContent(gywlwBrandMapper.selectByProductModel(plcInfo.getPlcType()).getDeviceKey());
                    plcInfo.setId(UUID.randomUUID().toString().replace("-", ""));
                    plcInfo.setDeviceId(device.getDeviceId());
                    plcInfo.setPlcName(plcInfo.getPlcBrand() + "0");
                    plcInfoList.add(plcInfo);
                }
            }
            //#1
            plcInfo = new GywlwPlcInfo();
            plcInfo.setSubdeviceId((String) map.get("serial1_subdev_id"));
            if(plcInfo.getSubdeviceId()!=null && !plcInfo.getSubdeviceId().equals("0")) {
                if (map.get("serial1_mode") != null) {
                    Integer serialxMode = Integer.parseInt((String) map.get("serial1_mode"));
                    plcInfo.setPlcBrand(brandMap.get(serialxMode));
                    plcInfo.setPlcType(typeMap.get(serialxMode));
                    plcInfo.setContent(gywlwBrandMapper.selectByProductModel(plcInfo.getPlcType()).getDeviceKey());
                    plcInfo.setId(UUID.randomUUID().toString().replace("-", ""));
                    plcInfo.setDeviceId(device.getDeviceId());
                    plcInfo.setPlcName(plcInfo.getPlcBrand() + "1");
                    plcInfoList.add(plcInfo);
                }
            }
            //#2
            plcInfo = new GywlwPlcInfo();
            plcInfo.setSubdeviceId((String) map.get("serial2_subdev_id"));
            if(plcInfo.getSubdeviceId()!=null && !plcInfo.getSubdeviceId().equals("0")) {
                if (map.get("serial2_mode") != null) {
                    Integer serialxMode = Integer.parseInt((String) map.get("serial2_mode"));
                    plcInfo.setPlcBrand(brandMap.get(serialxMode));
                    plcInfo.setPlcType(typeMap.get(serialxMode));
                    plcInfo.setContent(gywlwBrandMapper.selectByProductModel(plcInfo.getPlcType()).getDeviceKey());
                    plcInfo.setId(UUID.randomUUID().toString().replace("-", ""));
                    plcInfo.setDeviceId(device.getDeviceId());
                    plcInfo.setPlcName(plcInfo.getPlcBrand() + "2");
                    plcInfoList.add(plcInfo);
                }
            }
            //#3
            plcInfo = new GywlwPlcInfo();
            plcInfo.setSubdeviceId((String) map.get("serial3_subdev_id"));
            if(plcInfo.getSubdeviceId()!=null && !plcInfo.getSubdeviceId().equals("0")) {
                if (map.get("serial3_mode") != null) {
                    Integer serialxMode = Integer.parseInt((String) map.get("serial3_mode"));
                    plcInfo.setPlcBrand(brandMap.get(serialxMode));
                    plcInfo.setPlcType(typeMap.get(serialxMode));
                    plcInfo.setContent(gywlwBrandMapper.selectByProductModel(plcInfo.getPlcType()).getDeviceKey());
                    plcInfo.setId(UUID.randomUUID().toString().replace("-", ""));
                    plcInfo.setDeviceId(device.getDeviceId());
                    plcInfo.setPlcName(plcInfo.getPlcBrand() + "3");
                    plcInfoList.add(plcInfo);
                }
            }
            //#4
            plcInfo = new GywlwPlcInfo();
            plcInfo.setSubdeviceId((String) map.get("serial4_subdev_id"));
            if(plcInfo.getSubdeviceId()!=null && !plcInfo.getSubdeviceId().equals("0")) {
                if (map.get("serial4_mode") != null) {
                    Integer serialxMode = Integer.parseInt((String) map.get("serial4_mode"));
                    plcInfo.setPlcBrand(brandMap.get(serialxMode));
                    plcInfo.setPlcType(typeMap.get(serialxMode));
                    plcInfo.setContent(gywlwBrandMapper.selectByProductModel(plcInfo.getPlcType()).getDeviceKey());
                    plcInfo.setId(UUID.randomUUID().toString().replace("-", ""));
                    plcInfo.setDeviceId(device.getDeviceId());
                    plcInfo.setPlcName(plcInfo.getPlcBrand() + "4");
                    plcInfoList.add(plcInfo);
                }
            }
            break;//只取最新一条
        }
//        logger.info("更新数据库...");
        gywlwDeviceMapper.updateByPrimaryKeySelective(gywlwDevice);
//        gywlwPlcInfoMapper.deleteByDeviceId(device.getDeviceId());
        for(GywlwPlcInfo plc : plcInfoList) {
            GywlwPlcInfo plcInfo = gywlwPlcInfoMapper.selectBySubDeviceId(plc.getSubdeviceId());
            if(plcInfo == null) {
                gywlwPlcInfoMapper.insertSelective(plc);
            }else{
                plc.setId(plcInfo.getId());
                gywlwPlcInfoMapper.deleteByPrimaryKey(plcInfo.getId());
                gywlwPlcInfoMapper.insertSelective(plc);
            }
        }

    }


    public void handlerForPlcParams(String[] list1, GywlwDevice device){
        for(String str : list1){
            String gpioId = device.getGpioId();
            Long timestamp = device.getLastConnected().getTime();
            List<String> requestList = new ArrayList<>();
            requestList.add(str);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceId",gpioId);
            jsonObject.put("idList",requestList);
            jsonObject.put("timestamp",timestamp);
            System.out.println("请求参数: " + jsonObject);
//            logger.info("handlerForPlcParams准备工作结束，发送psot请求： " + new Date());
            String responseStr = null;
            try {
                responseStr = HttpsTest.postForm(jsonObject,1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(responseStr == null || responseStr.equals("0")){
                continue;
            }
//            System.out.println("待同步数据 : "+ responseStr);
            //handle response
//            JSONObject json = new JSONObject();
            Map<String, Object> map1 = JSON.parseObject(responseStr);
            List<Map> map2 = JSON.parseArray(map1.get("result_data").toString(),Map.class);
            PlcConfigModel model = JSON.parseObject(map2.get(0).get(str).toString(),PlcConfigModel.class);
//            System.out.println("response data:"+ list1);
//            logger.info("请求成功，开始处理数据： " + new Date());
                //更新plc中的subdeviceid
                GywlwPlcInfo gywlwPlcInfo = new GywlwPlcInfo();
                gywlwPlcInfo.setId(gywlwPlcInfoMapper.selectBySubDeviceId(model.getField_subdev_id()).getId());
                gywlwPlcInfo.setSubdeviceId(model.getField_subdev_id());
                if(gywlwPlcInfo.getSubdeviceId()!=null) {
                    gywlwPlcInfoMapper.updateByPrimaryKeySelective(gywlwPlcInfo);
                }
                //更新reginfo
                GywlwRegInfo gywlwRegInfo = new GywlwRegInfo();
                if(model.getField_rw() != null) {
                    gywlwRegInfo.setFieldRw(Integer.parseInt(model.getField_rw()));
                }
                gywlwRegInfo.setFieldFunctionCode(model.getField_functioncode());
                gywlwRegInfo.setRegName(model.getField_name());
                gywlwRegInfo.setRegAlias(model.getField_name2());
                gywlwRegInfo.setRegAddress(str);
                gywlwRegInfo.setFieldAttach(model.getField_attach());
                gywlwRegInfo.setPlcId(gywlwPlcInfo.getId());
                gywlwRegInfo.setDeviceId(device.getDeviceId());
                if(model.getField_type() != null) {
                    gywlwRegInfo.setFieldType(Integer.parseInt(model.getField_type()));
                }

//                logger.info("更新数据库...");
                if(gywlwPlcInfo.getSubdeviceId()!=null) {
                    gywlwPlcInfoMapper.updateByPrimaryKeySelective(gywlwPlcInfo);
                }
                //先删除已经存在的数据项，再插入新数据项
                if(gywlwRegInfo.getRegAddress()!=null) {
                    GywlwRegInfo regInfo = gywlwRegInfoMapper.selectByRegAddress(gywlwRegInfo.getRegAddress());
                    if(regInfo == null){
                        gywlwRegInfo.setRegId(UUID.randomUUID().toString().replaceAll("-",""));
                        gywlwRegInfoMapper.insertSelective(gywlwRegInfo);
                    }else{
                        gywlwRegInfo.setRegId(regInfo.getRegId());
                        gywlwRegInfoMapper.deleteByPrimaryKey(regInfo.getRegId());
                        gywlwRegInfoMapper.insertSelective(gywlwRegInfo);
                    }

                }
        }
    }




}
