package com.nbicc.gywlw.util;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

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




}
