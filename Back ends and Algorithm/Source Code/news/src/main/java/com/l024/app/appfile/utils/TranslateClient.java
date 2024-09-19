package com.l024.app.appfile.utils;

import china.mobile.common.demo.ApiUrlTest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class TranslateClient {

    private static String url = "/api/lingxiyun/cloud/translate/v1";
    public static String translateHttpUrl;
    private static String gatewayAddress = "https://api-wuxi-1.cmecloud.cn:8443";
    private static  String accessKey = "6a6f35f1504044d98a07f1343322437c";
    private static String secret =  "8a4e87d19a384afbad2931499dbdf61d";

    public static class TranslateEntity {
        public String text;
        public String type;
    }
    //翻译,encn为英->中，cnen为中->英
    public static String Translate(String text,String type) throws IOException {
        System.setProperty("javax.net.ssl.trustStore", "jssecacerts");
        ApiUrlTest urlTest = new ApiUrlTest();
        String urlpath = urlTest.doSignature(url, "POST", accessKey, secret);
        translateHttpUrl = gatewayAddress + urlpath;
        System.out.println(translateHttpUrl);
        TranslateEntity translateEntity=new TranslateEntity();
        translateEntity.text=text;
        translateEntity.type=type;

        HttpPost httpPost = new HttpPost(translateHttpUrl);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(JSON.toJSONString(translateEntity), ContentType.APPLICATION_JSON));
        //发送HTTP请求
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse httpResponse = httpClient.execute(httpPost);
            String response = EntityUtils.toString(httpResponse.getEntity());
            JSONObject jsonObject2 = JSONObject.parseObject(response);
            JSONObject body = (JSONObject) jsonObject2.get("body");
            JSONObject body2 = (JSONObject) body.get("body");
            String result = (String) body2.get("target");
            return result;
        }catch (Exception e){
            return null;
        }

    }



}
