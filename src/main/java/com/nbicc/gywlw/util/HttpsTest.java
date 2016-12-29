package com.nbicc.gywlw.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BigMao on 2016/12/12.
 */

public class HttpsTest {
    /**
     * HttpClient连接SSL
     */
//    public void ssl(){
//        CloseableHttpClient httpClient = null;
//        try{
//            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            FileInputStream inputStream = new FileInputStream(new File)
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        }
//    }
    public static void postForm() throws Exception {
        //创建默认实例
        String url = "https://121.40.90.27:8083/device/queryDeviceStatusHistory";
//        String url = "https://www.baidu.com";

//        CloseableHttpClient httpClient = new SSLClient();
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        // don't check
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        // don't check
                    }
                }
        };

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, trustAllCerts, null);

        LayeredConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(ctx);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslSocketFactory)
                .build();
//        HttpGet httpGet = new HttpGet(url);
        HttpPost httpPost = new HttpPost(url);
       // 设置参数
        List<String> list = new ArrayList<>();
        list.add("alarm1");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceId", "0052000000000003");//"0052000000000003"
        jsonObject.put("idList", list);
        jsonObject.put("timestamp", 1480767339L);

        StringEntity params =new StringEntity(jsonObject.toString());
        httpPost.addHeader("content-type", "application/json");
        httpPost.addHeader("Accept","application/json");
        httpPost.setEntity(params);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        // handle response here...
        System.out.println("---------------------------------");

        int status = response.getStatusLine().getStatusCode();
        if(status == 200){
            String result = EntityUtils.toString(response.getEntity(),"UTF-8");
            System.out.println("result: " + result);
        }
    }

//    public static void main(String[] args){
//            try {
//                postForm();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//    }
}

