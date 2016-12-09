package com.nbicc.gywlw.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by BigMao on 2016/11/17.
 */
public class MyUtil {
    private static final Logger logger = LoggerFactory.getLogger(MyUtil.class);
//    public static String getJSONString(int code) {
//        JSONObject json = new JSONObject();
//        json.put("result_code", code);
//        return json.toJSONString();
//    }
//
//    public static String getJSONString(int code, String msg) {
//        JSONObject json = new JSONObject();
//        json.put("result_code", code);
//        json.put("data", msg);
//        return json.toJSONString();
//    }

    public static JSONObject response(int code,Object object){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("result_code", code);
        jsonObject.put("data", object);
        return jsonObject;
    }


//    public static String getJSONString(int code, Object o) {
//        JSONObject json = new JSONObject();
//        json.put("result_code", code);
//        json.put("data", o);
//        return json.toJSONString();
//    }
//
//    public static String getJSONString(int code, Map<String, Object> map) {
//        JSONObject json = new JSONObject();
//        json.put("result_code", code);
//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            json.put(entry.getKey(), entry.getValue());
//        }
//        return json.toJSONString();
//    }
//
//    public static String getJSONString(int code, List list){
//        Map<String, Object> map = new HashMap<>();
//        map.put("result_code", 0);
//        map.put("data", list);
//        String returnString = JSON.toJSONString(map);
//        System.out.println("输出项目列表： " + returnString);
//        return returnString;
//    }

    public static String timeTransformToString(String time){
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long time1=Long.parseLong(time);
        String d = format.format(time1 * 1000);
        return d;
    }
    public static Date timeTransformToDate(String time) throws ParseException {
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long time1=Long.parseLong(time);
        String d = format.format(time1*1000);
        Date date=format.parse(d);
        return date;
    }
    public static Date timeTransformToDateNo1000(String time) throws ParseException {
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Long time1=Long.parseLong(time);
        String d = format.format(time1);
        Date date=format.parse(d);
        return date;
    }


    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }

}
