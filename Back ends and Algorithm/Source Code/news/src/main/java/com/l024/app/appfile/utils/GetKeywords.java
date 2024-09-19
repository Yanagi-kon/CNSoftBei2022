package com.l024.app.appfile.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinamobile.cmss.sdk.nlp.ECloudDefaultClient;
import com.chinamobile.cmss.sdk.nlp.http.constant.Region;
import com.chinamobile.cmss.sdk.nlp.http.signature.Credential;
import com.chinamobile.cmss.sdk.nlp.request.IECloudRequest;
import com.chinamobile.cmss.sdk.nlp.request.nlp.NlpRequestFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.max;

public class GetKeywords {
    public static String user_ak;
    private static String user_sk;
    private static ECloudDefaultClient client;

    static {
        user_ak = "6a6f35f1504044d98a07f1343322437c";
        user_sk = "8a4e87d19a384afbad2931499dbdf61d";
        Credential credential = new Credential(user_ak, user_sk);
        client = new ECloudDefaultClient(credential, Region.POOL_SZ);
    }

    //返回list，里面是两个list,第0个是关键词名字，第1个是权重
    //调用：GetKeywords.getKeywords(newsText),把用户所有读过的新闻文本相加放到newsText里就可以
    public static List<List> getKeywords(String newsText) {
        String news=Filter(newsText);  //过滤非汉字字符
        List<List> list2;
        List<String> NameList = new ArrayList<>();
        List<Double> WeightList = new ArrayList<>();
        String news1 = null;

        for(int i = 0;i< (news.length()/4000)+1; i++) {
            if(i== (news.length()/4000)){
                news1=news.substring(4000 * i);
            }else {
                news1=news.substring(4000 * i, 4000 * (i + 1));
            }

            list2 = testNlpKeyWords(news1);
            NameList.addAll(list2.get(0));
            WeightList.addAll(list2.get(1));
            for (int j = i*20; j < 20*i+(list2.get(1)).size(); j++) {
                WeightList.set(j, (WeightList.get(j))*news1.length());
            }
        }
        int maxindex;
        for(int i = 0;i<NameList.size();i++){
            for(int j = i+1;j<NameList.size();j++){
                if((NameList.get(i)).equals(NameList.get(j))){
                    NameList.remove(j);
                    WeightList.set(i,WeightList.get(i)+WeightList.get(j));
                    WeightList.remove(j);
                }
            }
        }

        List<String> keyname = new ArrayList<>();
        List<Double> keyweight = new ArrayList<>();
        int N=20;  //取20个词做词云
        for(int i = 0;i<N;i++){
            maxindex=WeightList.indexOf(max(WeightList));
            keyname.add(NameList.get(maxindex));
            keyweight.add(WeightList.get(maxindex));
            NameList.remove(maxindex);
            WeightList.remove(maxindex);
        }
        for (int i = 0; i < keyweight.size(); i++) {
            keyweight.set(i,(keyweight.get(i)));
        }

        List<List> keyList=new ArrayList<>();
        keyList.add(keyname);
        keyList.add(keyweight);
        return keyList;
    }
    public static List<List> testNlpKeyWords(String text) {
        List<List> lists=new ArrayList<>();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text",text);
        jsonObject.put("textId", "test_text_id");
        jsonObject.put("initial","words");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("items", jsonArray);
        IECloudRequest formRequest = NlpRequestFactory.getnlpRequest("/api/nlp/v1/keywords", jsonObject1);

        List<String> name=new ArrayList<>();
        List<Double> weight=new ArrayList<>();
        try {
            JSONObject response = (JSONObject) client.call(formRequest);
            JSONObject body= (JSONObject) response.get("body");
            ArrayList arrayList= (ArrayList) body.get("items");
            Map map= (Map) arrayList.get(0);

            ArrayList list= (ArrayList) map.get("keywords");
            for (int i = 0; i < list.size(); i++) {
                Map item= (Map) list.get(i);
                name.add((String) item.get("name"));
                weight.add((Double) item.get("weight"));
            }
            lists.add(name);
            lists.add(weight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lists;
    }
    public static String Filter(String buff){
        String tmpString =buff.replaceAll("[^\u4E00-\u9FA5]", "");;//去掉所有非汉字
        return tmpString;
    }







}
