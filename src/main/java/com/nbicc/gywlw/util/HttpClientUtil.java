package com.nbicc.gywlw.util;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
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
 * Created by BigMao on 2016/12/3.
 */
public class HttpClientUtil {
    public static final String ADD_URL1 = "https://iot-expeed.tech:8083/device/queryDeviceStatusHistory";
    public static final String ADD_URL2 = "https://iot-expeed.tech:8083/mongodb/queryStatusHistory";

    public static String post(JSONObject jsonObject, int k) {

        try {
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

            HttpPost request = new HttpPost();
            if(k==1) {
                URI url = new URI(ADD_URL1);
                request.setURI(url);
            }
            if(k==2){
                URI url = new URI(ADD_URL2);
                request.setURI(url);
            }
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
        }
        return "ok";
    }






}
