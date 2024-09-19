package com.l024.app.appfile.utils;

import china.mobile.common.demo.ApiUrlTest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.sound.sampled.*;
import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IatClient {
    private static String accessKey = "6a6f35f1504044d98a07f1343322437c";
    private static String secret = "8a4e87d19a384afbad2931499dbdf61d";
    private static String url = "/api/lingxiyun/cloud/iat/send_request/v1";
    private static String url2 = "/api/lingxiyun/cloud/iat/query_result/v1";
    private static String gatewayAddress = "https://api-wuxi-1.cmecloud.cn:8443";
    private static String iatHttpUrl;
    private static String sid;

    public IatClient() {
    }

    public static String SoundRec(MultipartFile file) throws Exception {
        System.setProperty("javax.net.ssl.trustStore", "jssecacerts");
        sid = UUID.randomUUID().toString();
        ApiUrlTest urlTest = new ApiUrlTest();
        String urlpath = urlTest.doSignature(url, "POST", accessKey, secret);
        iatHttpUrl = gatewayAddress + urlpath;
        Map<String, String> sessionParam = new HashMap();
        sessionParam.put("sid", sid);
        sessionParam.put("aue", "raw");
        sessionParam.put("rate", "16000");
        sessionParam.put("rst", "plain");
        sessionParam.put("rse", "utf8");
        sessionParam.put("bos", "30000");
        sessionParam.put("eos", "30000");
        sessionParam.put("dwa", "wpgs");
        String fileNameMp = UUID.randomUUID().toString().replace("-", "") + ".mp3";
        String fileNameWav = UUID.randomUUID().toString().replace("-", "") + ".wav";
        String path = "d:\\tt\\"+fileNameMp;
        String pathWav = "d:\\tt\\"+fileNameWav;


        File fileLocal = new File(path);
        file.transferTo(fileLocal);
        byteToWav(getBytes(path), pathWav);
        String msg = (new IatClient()).sendFile(pathWav, sessionParam, 40960);
        System.out.println(msg);
        Thread.sleep(1000L);
        return getResult();
    }
    public static byte[] getBytes(String filepath) {
        byte[] buffer = null;
        try {
            File file = new File(filepath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static boolean byteToWav(byte[] sourceBytes, String targetPath) {
        if (sourceBytes == null || sourceBytes.length == 0) {
            System.out.println("Illegal Argument passed to this method");
            return false;
        }
        try (final ByteArrayInputStream bais = new ByteArrayInputStream(sourceBytes); final AudioInputStream sourceAIS = AudioSystem.getAudioInputStream(bais)) {
            AudioFormat sourceFormat = sourceAIS.getFormat();
            AudioFormat mp3tFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
            AudioFormat pcmFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000, 16, 1, 2, 16000, false);
            try (
                    final AudioInputStream mp3AIS = AudioSystem.getAudioInputStream(mp3tFormat, sourceAIS);
                    final AudioInputStream pcmAIS = AudioSystem.getAudioInputStream(pcmFormat, mp3AIS)) {
                AudioSystem.write(pcmAIS, AudioFileFormat.Type.WAVE, new File(targetPath));
            }
            return true;
        } catch (IOException e) {
            System.out.println("文件转换异常：" + e.getMessage());
            return false;
        } catch (UnsupportedAudioFileException e) {
            System.out.println("文件转换异常：" + e.getMessage());
            return false;
        }

    }

    public String sendFile(String filePath, Map<String, String> sessionParam, int sliceSize) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            return "文件不存在:" + file.getAbsolutePath();
        } else {
            int sliceNum = (int) Math.ceil((double) file.length() / (double) sliceSize);

            for (int i = 0; i < sliceNum; ++i) {
                byte[] data = getData(file, i * sliceSize, sliceSize);
                if (data.length == 0) {
                    break;
                }

                if (i > 0) {
                    sessionParam = null;
                }

                this.send(String.valueOf(i + 1), sessionParam, data, i == sliceNum - 1 ? 1 : 0);
            }

            return "分片发送完成，等待接收转写结果";
        }
    }

    public void send(String number, Map<String, String> sessionParam, byte[] audioBytes, int endFlag) throws Exception {
        HttpPost httpPost = new HttpPost(iatHttpUrl);
        httpPost.setHeader("streamId", sid);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("number", number);
        httpPost.setHeader("language", "cn");
        String audioStr = Base64.getEncoder().encodeToString(audioBytes);
        IatReqVo vo = new IatReqVo();
        vo.data = audioStr;
        vo.endFlag = endFlag;
        vo.sessionParam = sessionParam;
        httpPost.setEntity(new StringEntity(JSON.toJSONString(vo), ContentType.APPLICATION_JSON));
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String response = EntityUtils.toString(httpResponse.getEntity());
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (jsonObject != null) {
            if (jsonObject.get("body") == null || jsonObject.getJSONArray("body").size() > 0) {
                System.out.println(response);
            }
        } else {
            System.out.println("no response.");
        }

    }

    public static byte[] getData(File file, int from, int size) throws Exception {
        InputStream in = null;

        byte[] var5;
        try {
            in = new FileInputStream(file);
            byte[] tempbytes = new byte[Math.min(in.available(), size)];
            in = new FileInputStream(file);
            in.skip((long) from);
            in.read(tempbytes);
            var5 = tempbytes;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var12) {
                }
            }

        }

        return var5;
    }

    public static String getResult() throws Exception {
        ApiUrlTest urlTest = new ApiUrlTest();
        String urlpath = urlTest.doSignature(url2, "GET", accessKey, secret);
        iatHttpUrl = gatewayAddress + urlpath;
        HttpGet httpGet = new HttpGet(iatHttpUrl);
        httpGet.setHeader("streamId", sid);
        httpGet.setHeader("Content-Type", "application/json");
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = httpClient.execute(httpGet);
        String response = EntityUtils.toString(httpResponse.getEntity());
        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONObject body = (JSONObject) jsonObject.get("body");
        JSONArray jsonArray = (JSONArray) body.get("frame_results");
        String result = "";
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
            result = result + jsonObject1.get("ansStr");
        }
        if (jsonObject != null) {
            System.out.println("转写结果：" + result);
        } else {
            System.out.println("无转写结果");
        }

        return result;
    }

    public static class IatReqVo {
        public Map<String, String> sessionParam;
        public String data;
        public Integer endFlag;

        public IatReqVo() {
        }
    }
}
