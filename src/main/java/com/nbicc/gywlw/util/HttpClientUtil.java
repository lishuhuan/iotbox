package com.nbicc.gywlw.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by BigMao on 2016/12/3.
 */
public class HttpClientUtil {
    public static final String ADD_URL = "http://121.40.90.27:8083/device/queryDeviceStatusHistory";

    public static String post(JSONObject jsonObject) {
        HttpClient httpClient = new DefaultHttpClient();

        try {
            HttpPost request = new HttpPost(ADD_URL);
            StringEntity params =new StringEntity(jsonObject.toString());
            request.addHeader("content-type", "application/json");
            request.addHeader("Accept","application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

            // handle response here...
            System.out.println("---------------------------------");

            int status = response.getStatusLine().getStatusCode();
            if(status == 200){
                String result = EntityUtils.toString(response.getEntity(),"UTF-8");
                return result;
            }

        }catch (Exception ex) {
            // handle exception here
            ex.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return "ok";
    }

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceId","0052000000000003");
        List list = new ArrayList();
        list.add("alarm1");
        list.add("alarm2");
        jsonObject.put("idList",list);
        Long timestamp = 1480767339L;
        jsonObject.put("timestamp",timestamp);
        String str = post(jsonObject);
//        System.out.println(str);
        JSONObject json = new JSONObject();
        Map<String, Object> map1 = JSON.parseObject(str);
        List<Map> list1 = JSON.parseArray(map1.get("result_data").toString(),Map.class);
        System.out.println("result_data2:");
        for(Map map:list1){
            if(map.get("alarm2")==null){
                System.out.println("null");
            }else {
                System.out.println(map.get("alarm2"));
            }
        }

    }
}
