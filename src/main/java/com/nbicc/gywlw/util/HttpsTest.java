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
import java.net.URI;
import java.security.cert.X509Certificate;

/**
 * Created by BigMao on 2016/12/12.
 * 无用
 */

public class HttpsTest {
    /**
     * HttpClient连接SSL
     */
    public static final String ADD_URL1 = "https://iot-expeed.tech:8083/device/queryDeviceStatusHistory";
    public static final String ADD_URL2 = "https://iot-expeed.tech:8083/mongodb/queryStatusHistory";

    //    public void ssl(){
//        CloseableHttpClient httpClient = null;
//        try{
//            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            FileInputStream inputStream = new FileInputStream(new File)
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        }
//    }
    public static String postForm(JSONObject jsonObject, int k) throws Exception {
        //创建默认实例
//        String url = "https://iot-expeed.tech:8445/test";
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
//        HttpPost httpPost = new HttpPost(url);
        HttpPost request = new HttpPost();
        if(k==1) {
            URI url = new URI(ADD_URL1);
            request.setURI(url);
        }
        if(k==2){
            URI url = new URI(ADD_URL2);
            request.setURI(url);
        }
       // 设置参数
//        List<String> list = new ArrayList<>();
//        list.add("alarm1");
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("deviceId", "0052000000000003");//"0052000000000003"
//        jsonObject.put("idList", list);
//        jsonObject.put("timestamp", 1480767339L);
//
//        StringEntity params =new StringEntity(jsonObject.toString());
//        httpPost.addHeader("content-type", "application/json");
//        httpPost.addHeader("Accept","application/json");
//        httpPost.setEntity(params);
        StringEntity params =new StringEntity(jsonObject.toString());
        request.addHeader("content-type", "application/json");
        request.addHeader("Accept","application/json");
        request.setEntity(params);

        CloseableHttpResponse response = httpClient.execute(request);
        // handle response here...
        System.out.println("---------------------------------");

        int status = response.getStatusLine().getStatusCode();
        if(status == 200){
            String result = EntityUtils.toString(response.getEntity(),"UTF-8");
            return result;
        }
        return "0";
    }

//    public static void main(String[] args){
//            try {
//                List<String> requestList = new ArrayList<>();
//                List<String> gpioList = new ArrayList<>();
//                gpioList.add("field_autocollect");
//                gpioList.add("field_subdev_id");
//                gpioList.add("field_type");
//                gpioList.add("field_name");
//                gpioList.add("field_name2");
//                gpioList.add("field_functioncode");
//                gpioList.add("field_attach");
//                gpioList.add("field_rw");
//                requestList.addAll(gpioList);
////                requestList.add("time");
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("deviceId","0053000000000004");
//                jsonObject.put("idList",requestList);
//                jsonObject.put("timestamp",1483424268004L);
//                System.out.println("请求参数: " + jsonObject);
//
////                logger.info("准备工作结束，发送psot请求： " + new Date());
//
//          //      String str = HttpClientUtil.post(jsonObject,1);
//                String str = postForm(jsonObject,1);
//                System.out.println("待同步数据 : "+ str);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//    }
}

