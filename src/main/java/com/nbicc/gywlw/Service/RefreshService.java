package com.nbicc.gywlw.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nbicc.gywlw.Model.*;
import com.nbicc.gywlw.mapper.*;
import com.nbicc.gywlw.util.HttpsTest;
import com.nbicc.gywlw.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public void refresh(){
//        refreshConfigParams();
//        refreshPlcParams();
        refreshDataForGpio();
        refreshDataForPlc();

    }

    //PLC，默认一个物联网盒子上连接的plc都是同一型号
    public void refreshDataForPlc(){
        logger.info("同步plc数据准备工作： " + new Date());
//        JedisPool pool = RedisAPI.getPool();
//        Jedis jedis = pool.getResource();
        List<GywlwDevice> devices = gywlwDeviceMapper.selectAll();
        if(devices.size() != 0) {
            for (GywlwDevice device : devices) {
                List<String> deviceIdList = new ArrayList<>();
                String sdkKey = "";
                Long timestamp;
//                if(jedis.get(device.getDeviceSn())!=null){
//                    timestamp = Long.parseLong(jedis.get(device.getDeviceSn()));
//                }else{
//                    timestamp = System.currentTimeMillis() - 1*86400000L; //当前时间减1天
//                }
//                Long timestamp = 1482798633000L;//test
                timestamp = System.currentTimeMillis() - 1*86400000L; //当前时间减1天
                List<String> regList = new ArrayList<>();
                List<String> requestList = new ArrayList<>();
                List<GywlwPlcInfo> plcinfos = gywlwPlcInfoMapper.selectByDeviceId(device.getDeviceId());
                if (plcinfos.size() != 0) {
                    for (GywlwPlcInfo plcInfo : plcinfos) {
                        deviceIdList.add(plcInfo.getSubdeviceId());
                        sdkKey = plcInfo.getContent(); //content保存着sdkkey
                        List<GywlwRegInfo> reglists = gywlwRegInfoMapper.selectByPlcId(plcInfo.getId());
                        if(reglists.size() != 0){
                            for(GywlwRegInfo reg: reglists){
                                regList.add(reg.getRegName());
                            }
                        }
                    }
                }else{
                    continue;
                }
                requestList.addAll(regList);
                requestList.add("time");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deviceIdList",deviceIdList);
                jsonObject.put("sdkKey",sdkKey);
                jsonObject.put("timestamp",timestamp);
                jsonObject.put("idList",requestList);

                System.out.println("请求参数: " + jsonObject);
                logger.info("准备工作结束，发送psot请求： " + new Date());
                String str = null;
                try {
                    str = HttpsTest.postForm(jsonObject,2);  //2代表plc通道，也就是走子设备通道
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("待同步数据 : "+ str);
                //handle response
                if(str.equals("0")){
                    continue;
                }
                JSONObject json = new JSONObject();
                Map<String, Object> map1 = JSON.parseObject(str);
                List<Map> list1 = JSON.parseArray(map1.get("result_data").toString(), Map.class);
   //             System.out.println("list1.size():"+list1.toString());
                logger.info("请求成功，开始处理数据： " + new Date());
                if(list1.size() > 0){
                    try {
//                        jedis.set(device.getDeviceSn(), list1.get(0).get("timestamp").toString());
                        //单线程
//                        handlerForPlc(list1,regList);
                        //多线程
                        LinkedList<Map> linkedList = new LinkedList<>();
                        linkedList.addAll(list1);
                        handleByThread(linkedList,regList,15);  //同时处理的线程数量多少最高效？
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
    }

    //GPIO
    public void refreshDataForGpio() {
        logger.info("同步gpio数据准备工作： " + new Date());
        List<GywlwDevice> devices = gywlwDeviceMapper.selectAll();
        if(devices.size() != 0){
            for (GywlwDevice device : devices) {
                String gpioId = device.getGpioId();
                if(gpioId == null){
                    continue;
                }
                Long timestamp;
//                JedisPool pool = RedisAPI.getPool();
//                Jedis jedis = pool.getResource();
//                if(jedis.get(device.getGpioId())!=null){
//                    timestamp = Long.parseLong(jedis.get(device.getGpioId()));
//                }else{
//                    timestamp = System.currentTimeMillis() - 86400L; //当前时间减1天
//                }
                timestamp = System.currentTimeMillis() - 86400L; //当前时间减1天
                List<String> requestList = new ArrayList<>();
                List<String> gpioList = new ArrayList<>();
                gpioList.add("gpio_1");
                gpioList.add("gpio_2");
                gpioList.add("gpio_3");
                gpioList.add("gpio_4");
                gpioList.add("gpio_5");
                gpioList.add("gpio_6");
                gpioList.add("gpio_7");
                gpioList.add("gpio_8");
                requestList.addAll(gpioList);
                requestList.add("time");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deviceId",gpioId);
                jsonObject.put("idList",requestList);
                jsonObject.put("timestamp",timestamp);
                System.out.println("请求参数: " + jsonObject);

                logger.info("准备工作结束，发送psot请求： " + new Date());

                String str = null;
                try {
                    str = HttpsTest.postForm(jsonObject,1); //1代表走设备通道
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("待同步数据 : "+ str);
                //handle response
                if(str.equals("0")){
                    continue;
                }
                JSONObject json = new JSONObject();
                Map<String, Object> map1 = JSON.parseObject(str);
                List<GpioDataModel> list1 = JSON.parseArray(map1.get("result_data").toString(), GpioDataModel.class);
//                System.out.println("response data:"+ list1);
                logger.info("请求成功，开始处理数据： " + new Date());
                if(list1.size() != 0){
                    //储存该设备最新一次更新的时间戳
//                    jedis.set(device.getGpioId(),list1.get(0).getTimestamp());
                    handler(list1,device,gpioList);
                }
            }
        }
    }

    //todo 数据还没有
    public void refreshRulesForPlc(){

    }

    public void refreshRulesForGpio(){

    }

    public void refreshConfigParams(){
        logger.info("同步设备配置的准备工作： " + new Date());
        List<GywlwDevice> devices = gywlwDeviceMapper.selectAll();
        if(devices.size() != 0) {
            for (GywlwDevice device : devices) {
                String gpioId = device.getGpioId();
                if (gpioId == null) {
                    continue;
                }
                Long timestamp;
//                JedisPool pool = RedisAPI.getPool();
//                Jedis jedis = pool.getResource();
//                if(jedis.get(device.getGpioId())!=null){
//                    timestamp = Long.parseLong(jedis.get(device.getGpioId()));
//                }else{
//                    timestamp = System.currentTimeMillis() - 86400L; //当前时间减1天
//                }
//                timestamp = System.currentTimeMillis() - 86400L; //当前时间减1天
                timestamp = device.getLastConnected().getTime();
                List<String> requestList = new ArrayList<>();
                requestList.add("hardware_edition");
                requestList.add("software_edition");
                requestList.add("3g_mode");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deviceId",gpioId);
                jsonObject.put("idList",requestList);
                jsonObject.put("timestamp",timestamp);
                System.out.println("请求参数: " + jsonObject);

                logger.info("准备工作结束，发送psot请求： " + new Date());

                String str = null;
                try {
                    str = HttpsTest.postForm(jsonObject,1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("待同步数据 : "+ str);
                //handle response
                JSONObject json = new JSONObject();
                Map<String, Object> map1 = JSON.parseObject(str);
                List<Map> list1 = JSON.parseArray(map1.get("result_data").toString(), Map.class);
//                System.out.println("返回数据:  "+list1.toString());
                logger.info("请求成功，开始处理数据： " + new Date());
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

    public void refreshGpioParams(){
        logger.info("同步Gpio配置信息的准备工作： " + new Date());

    }

    //还没测试好，因为平台那边只保留一个autocollect
    public void refreshPlcParams(){
        logger.info("同步plc配置(通道信息)的准备工作： " + new Date());
        List<GywlwDevice> devices = gywlwDeviceMapper.selectAll();
        if(devices.size() != 0) {
            for (GywlwDevice device : devices) {
                String gpioId = device.getGpioId();
                if (gpioId == null) {
                    continue;
                }
                Long timestamp;
//                JedisPool pool = RedisAPI.getPool();
//                Jedis jedis = pool.getResource();
//                if(jedis.get(device.getGpioId())!=null){
//                    timestamp = Long.parseLong(jedis.get(device.getGpioId()));
//                }else{
//                    timestamp = System.currentTimeMillis() - 86400L; //当前时间减1天
//                }
//                timestamp = System.currentTimeMillis() - 86400L; //当前时间减1天
                timestamp = device.getLastConnected().getTime();
                List<String> requestList = new ArrayList<>();
                requestList.add("field_autocollect");
                requestList.add("field_subdev_id");
                requestList.add("field_type");
                requestList.add("field_name");
                requestList.add("field_name2");
                requestList.add("field_functioncode");
                requestList.add("field_attach");
                requestList.add("field_rw");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deviceId",gpioId);
                jsonObject.put("idList",requestList);
                jsonObject.put("timestamp",timestamp);
                System.out.println("请求参数: " + jsonObject);

                logger.info("准备工作结束，发送psot请求： " + new Date());

                String str = null;
                try {
                    str = HttpsTest.postForm(jsonObject,1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("待同步数据 : "+ str);
                //handle response
                JSONObject json = new JSONObject();
                Map<String, Object> map1 = JSON.parseObject(str);
                List<Map> list1 = JSON.parseArray(map1.get("result_data").toString(), Map.class);
//                System.out.println("返回数据:  "+list1.toString());
                logger.info("请求成功，开始处理数据： " + new Date());
                if(list1.size() > 0){
                    handlerForPlcParams(list1,device);
                }

            }
        }
    }

    //用了硬编码，处理速度还可以，就没有用多线程
    private void handler(List<GpioDataModel> list, GywlwDevice device, List<String> gpioList){
        try {
            List<GywlwHistoryDataForGPIO> listForGpio = new ArrayList<>();
            for (GpioDataModel model : list) {
//            System.out.println(model.getTime());
                badAdd(listForGpio,model,device);
            }
            logger.info("批量插入数据开始");
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
        gywlwHistoryDataForGPIO.setContent(model.getTimestamp());
        gywlwHistoryDataForGPIO.setTime(MyUtil.transformToDate(model.getTime()));
        gywlwHistoryDataForGPIO.setDeviceId(device.getDeviceId());
        gywlwHistoryDataForGPIO.setGpioId("1");
        gywlwHistoryDataForGPIO.setValue(model.getGpio_1().getValue());
        if(model.getGpio_1().getAlarm()!=0){
            String alarm = "";
            if(model.getGpio_1().getAlarm1() != null) {
                alarm = alarm + model.getGpio_1().getAlarm1();
            }
            if(model.getGpio_1().getAlarm2() != null) {
                alarm = alarm + " " + model.getGpio_1().getAlarm2();
            }
            gywlwHistoryDataForGPIO.setAlarm(alarm);
        }
        listForGpio.add(gywlwHistoryDataForGPIO);

        //#2
        gywlwHistoryDataForGPIO = new GywlwHistoryDataForGPIO();
        gywlwHistoryDataForGPIO.setContent(model.getTimestamp());
        gywlwHistoryDataForGPIO.setTime(MyUtil.transformToDate(model.getTime()));
        gywlwHistoryDataForGPIO.setDeviceId(device.getDeviceId());
        gywlwHistoryDataForGPIO.setGpioId("2");
        gywlwHistoryDataForGPIO.setValue(model.getGpio_2().getValue());
        if(model.getGpio_2().getAlarm()!=0){
            String alarm = "";
            if(model.getGpio_2().getAlarm1() != null) {
                alarm = alarm + model.getGpio_2().getAlarm1();
            }
            if(model.getGpio_2().getAlarm2() != null) {
                alarm = alarm + " " + model.getGpio_2().getAlarm2();
            }
            gywlwHistoryDataForGPIO.setAlarm(alarm);
        }
        listForGpio.add(gywlwHistoryDataForGPIO);


        //#3
        gywlwHistoryDataForGPIO = new GywlwHistoryDataForGPIO();
        gywlwHistoryDataForGPIO.setContent(model.getTimestamp());
        gywlwHistoryDataForGPIO.setTime(MyUtil.transformToDate(model.getTime()));
        gywlwHistoryDataForGPIO.setDeviceId(device.getDeviceId());
        gywlwHistoryDataForGPIO.setGpioId("3");
        gywlwHistoryDataForGPIO.setValue(model.getGpio_3().getValue());
        if(model.getGpio_3().getAlarm()!=0){
            String alarm = "";
            if(model.getGpio_3().getAlarm1() != null) {
                alarm = alarm + model.getGpio_3().getAlarm1();
            }
            if(model.getGpio_3().getAlarm2() != null) {
                alarm = alarm + " " + model.getGpio_3().getAlarm2();
            }
            gywlwHistoryDataForGPIO.setAlarm(alarm);
        }
        listForGpio.add(gywlwHistoryDataForGPIO);

        //#4
        gywlwHistoryDataForGPIO = new GywlwHistoryDataForGPIO();
        gywlwHistoryDataForGPIO.setContent(model.getTimestamp());
        gywlwHistoryDataForGPIO.setTime(MyUtil.transformToDate(model.getTime()));
        gywlwHistoryDataForGPIO.setDeviceId(device.getDeviceId());
        gywlwHistoryDataForGPIO.setGpioId("4");
        gywlwHistoryDataForGPIO.setValue(model.getGpio_4().getValue());
        if(model.getGpio_4().getAlarm()!=0){
            String alarm = "";
            if(model.getGpio_4().getAlarm1() != null) {
                alarm = alarm + model.getGpio_4().getAlarm1();
            }
            if(model.getGpio_4().getAlarm2() != null) {
                alarm = alarm + " " + model.getGpio_4().getAlarm2();
            }
            gywlwHistoryDataForGPIO.setAlarm(alarm);
        }
        listForGpio.add(gywlwHistoryDataForGPIO);

        //#5
        gywlwHistoryDataForGPIO = new GywlwHistoryDataForGPIO();
        gywlwHistoryDataForGPIO.setContent(model.getTimestamp());
        gywlwHistoryDataForGPIO.setTime(MyUtil.transformToDate(model.getTime()));
        gywlwHistoryDataForGPIO.setDeviceId(device.getDeviceId());
        gywlwHistoryDataForGPIO.setGpioId("5");
        gywlwHistoryDataForGPIO.setValue(model.getGpio_5().getValue());
        if(model.getGpio_5().getAlarm()!=0){
            String alarm = "";
            if(model.getGpio_5().getAlarm1() != null) {
                alarm = alarm + model.getGpio_5().getAlarm1();
            }
            if(model.getGpio_5().getAlarm2() != null) {
                alarm = alarm + " " + model.getGpio_5().getAlarm2();
            }
            gywlwHistoryDataForGPIO.setAlarm(alarm);
        }
        listForGpio.add(gywlwHistoryDataForGPIO);

        //#6
        gywlwHistoryDataForGPIO = new GywlwHistoryDataForGPIO();
        gywlwHistoryDataForGPIO.setContent(model.getTimestamp());
        gywlwHistoryDataForGPIO.setTime(MyUtil.transformToDate(model.getTime()));
        gywlwHistoryDataForGPIO.setDeviceId(device.getDeviceId());
        gywlwHistoryDataForGPIO.setGpioId("6");
        gywlwHistoryDataForGPIO.setValue(model.getGpio_6().getValue());
        if(model.getGpio_6().getAlarm()!=0){
            String alarm = "";
            if(model.getGpio_6().getAlarm1() != null) {
                alarm = alarm + model.getGpio_6().getAlarm1();
            }
            if(model.getGpio_6().getAlarm2() != null) {
                alarm = alarm + " " + model.getGpio_6().getAlarm2();
            }
            gywlwHistoryDataForGPIO.setAlarm(alarm);
        }
        listForGpio.add(gywlwHistoryDataForGPIO);

        //#7
        gywlwHistoryDataForGPIO = new GywlwHistoryDataForGPIO();
        gywlwHistoryDataForGPIO.setContent(model.getTimestamp());
        gywlwHistoryDataForGPIO.setTime(MyUtil.transformToDate(model.getTime()));
        gywlwHistoryDataForGPIO.setDeviceId(device.getDeviceId());
        gywlwHistoryDataForGPIO.setValue(model.getGpio_7().getValue());
        gywlwHistoryDataForGPIO.setGpioId("7");
        if(model.getGpio_7().getAlarm()!=0){
            String alarm = "";
            if(model.getGpio_7().getAlarm1() != null) {
                alarm = alarm + model.getGpio_7().getAlarm1();
            }
            if(model.getGpio_7().getAlarm2() != null) {
                alarm = alarm + " " + model.getGpio_7().getAlarm2();
            }
            gywlwHistoryDataForGPIO.setAlarm(alarm);
        }
        listForGpio.add(gywlwHistoryDataForGPIO);

        //#8
        gywlwHistoryDataForGPIO = new GywlwHistoryDataForGPIO();
        gywlwHistoryDataForGPIO.setContent(model.getTimestamp());
        gywlwHistoryDataForGPIO.setTime(MyUtil.transformToDate(model.getTime()));
        gywlwHistoryDataForGPIO.setDeviceId(device.getDeviceId());
        gywlwHistoryDataForGPIO.setValue(model.getGpio_8().getValue());
        gywlwHistoryDataForGPIO.setGpioId("8");
        if(model.getGpio_8().getAlarm()!=0){
            String alarm = "";
            if(model.getGpio_8().getAlarm1() != null) {
                alarm = alarm + model.getGpio_8().getAlarm1();
            }
            if(model.getGpio_8().getAlarm2() != null) {
                alarm = alarm + " " + model.getGpio_8().getAlarm2();
            }
            gywlwHistoryDataForGPIO.setAlarm(alarm);
        }
        listForGpio.add(gywlwHistoryDataForGPIO);
    }

    public void handlerForPlc(List<Map> list1, List<String> regList) throws ParseException, CloneNotSupportedException {
        List<GywlwHistoryItem> historyItemList = new ArrayList<>();
        for(Map map : list1){
//            System.out.println(map.get("device_id"));
//            System.out.println(map.get("Y0002"));
//            Map map1 = (JSONObject) JSON.parse(map.get("Y0002").toString());
//            System.out.println(map1.get("value"));
            GywlwHistoryItem gywlwHistoryItem = new GywlwHistoryItem();
            gywlwHistoryItem.setItemTime(MyUtil.transformToDate((String) map.get("time")));
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
                gywlwHistoryItem.setRegId(gywlwRegInfoMapper.selectByRegName(reg).getRegId());
                gywlwHistoryItem.setItemAddress(reg);
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

    public synchronized void handleByThread(LinkedList<Map> list1, List<String> regList, int threadNum)
                throws InterruptedException, ParseException, CloneNotSupportedException {
        int length = list1.size();
        int t1 = length % threadNum == 0 ? length / threadNum : (length / threadNum + 1);
        CountDownLatch latch = new CountDownLatch(threadNum);// 多少协作
        long a = System.currentTimeMillis();
        if(length < threadNum * 3){
            handlerForPlc(list1, regList);
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
                            list1, start, end, latch, regList);
                    Thread runable = new Thread(thread);
                    runable.start();
                }
            }
            latch.await();// 等待所有工人完成工作
        }
        System.out.println("结束*****************************");
        long b = System.currentTimeMillis();
        System.out.println("时间:" + (b - a) + "毫秒***********************");
    }

    class RunnableThread implements Runnable {
        private String threadName;
        private List data;
        private int start;
        private int end;
        private CountDownLatch latch;
        private List<String> regList;

        public RunnableThread(String threadName, List data, int start, int end, CountDownLatch latch,
                              List<String> regList) {
            this.threadName = threadName;
            this.data = data;
            this.start = start;
            this.end = end;
            this.latch = latch;
            this.regList = regList;
        }

        public void run() {
            // 这里处理数据
            logger.info("start: " + start + " ||  end: " + end);
            List subList = data.subList(start, end);  //[start,end)
            System.out.println(threadName + "--" + data.size() + "--" + start + "--" + end + "--");
            // 单个线程中的数据
            try {
                handlerForPlc(subList, regList);
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
        for(Map map : list1){
            gywlwDevice.setHardwareEdition((String) map.get("hardware_edition"));
            gywlwDevice.setSoftwareEdition((String) map.get("software_edition"));
            gywlwDevice.setThreeGMode(Integer.parseInt((String) map.get("3g_mode")));
            gywlwDevice.setLastConnected(MyUtil.timeTransformToDateNo1000(map.get("timestamp").toString()));
            break;//只取最新一条
        }
        logger.info("更新数据库...");
        gywlwDeviceMapper.updateByPrimaryKeySelective(gywlwDevice);
    }

    //还没测试好，因为平台那边只保留一个autocollect
    public void handlerForPlcParams(List<Map> list1, GywlwDevice device){
        GywlwPlcInfo gywlwPlcInfo = new GywlwPlcInfo();
        GywlwRegInfo gywlwRegInfo;
        List<GywlwPlcInfo> plcInfos = gywlwPlcInfoMapper.selectByDeviceId(device.getDeviceId());
        //因为目前默认一个盒子只绑定一个plc
        for(GywlwPlcInfo plcInfo : plcInfos){
            gywlwPlcInfo.setId(plcInfo.getId());
            break;
        }
        for(Map map : list1){
            gywlwRegInfo = new GywlwRegInfo();
            if(map.get("field_autocollect") == null){
                continue;
            }
            gywlwPlcInfo.setSubdeviceId((String) map.get("field_subdev_id"));
            gywlwRegInfo.setDeviceId(device.getDeviceId());
            gywlwRegInfo.setPlcId(gywlwPlcInfo.getId());
            gywlwRegInfo.setRegAddress((String) map.get("field_autocollect"));
            gywlwRegInfo.setRegName((String) map.get("field_name"));
            gywlwRegInfo.setRegAlias((String) map.get("field_name2"));
            if(map.get("field_type") != null){
                gywlwRegInfo.setFieldType(Integer.parseInt((String) map.get("field_type")));
            }
            if(map.get("field_rw") != null){
                gywlwRegInfo.setFieldRw(Integer.parseInt((String) map.get("field_rw")));
            }
            gywlwRegInfo.setFieldFunctionCode((String) map.get("field_functioncode"));
            gywlwRegInfo.setFieldAttach((String) map.get("field_attach"));

            logger.info("更新数据库...");
//        System.out.println("gywlwplcinfo:  " + MyUtil.response(0,gywlwPlcInfo));
            if(gywlwPlcInfo.getSubdeviceId()!=null) {
                gywlwPlcInfoMapper.updateByPrimaryKeySelective(gywlwPlcInfo);
            }
            //先删除已经存在的数据项，再插入新数据项
            if(gywlwRegInfo.getRegAddress()!=null) {
                gywlwRegInfoMapper.deleteByPlcId(gywlwPlcInfo.getId());
                gywlwRegInfo.setRegId(UUID.randomUUID().toString().replaceAll("-",""));
                gywlwRegInfoMapper.insertSelective(gywlwRegInfo);
            }

        }

    }





}
