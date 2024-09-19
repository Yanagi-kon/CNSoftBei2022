package com.l024.app.appfile.utils.crawler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.l024.app.appfile.entity.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsCrawl {
    public static String url = "http://society.people.com.cn/"; //人民网 经济科技栏目
    public static String oldUrl = url;
    public static List<String> titleList = new ArrayList<>();
    public static List<String> UrlPath= Arrays.asList(
            new String[]{"http://finance.people.com.cn/","http://society.people.com.cn/","http://ent.people.com.cn/",
                    "http://opinion.people.com.cn/","http://world.people.com.cn/","http://military.people.com.cn/",
                    "http://hm.people.com.cn/","http://tw.people.com.cn/","http://edu.people.com.cn/",
                    "http://cpc.people.com.cn/","http://health.people.com.cn/"});




    public static void main(String[] args) throws IOException {
        List list = GetUrl(0);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            GetNews(it.next().toString(), 0);
        }

   }

    public static News GetNews(String url, int type) {
        Elements news = null;
        String tag = null;
        String title = null;

        String time = null;
        String text = null;
        String picB64 = null;
        JSONArray Text = new JSONArray();
        StringBuffer buffer = new StringBuffer();;
        try {
            Document document = Jsoup.connect(url).get();

            oldUrl= UrlPath.get(type);
            tag = retTag(oldUrl);//分类

            news = document.select("div.col.col-1.fl>h1");
            title = news.text(); //标题
            if (titleList.contains(title)||title.equals(""))
                return null;
            else titleList.add(title);

            news = document.select("div.col-1-1.fl");
            time = news.text(); //时间
            time=time.substring(0,11);
            time=time.replace('年', '.');
            time=time.replace('月', '.');
            time=time.replace('日', '.');
            buffer.append(time + "\n");


            news = document.select("div.rm_txt_con.cf>p");  //人民网几乎文本都在这里
            for (Element txt : news) {

                if (!txt.select("img").isEmpty()) { //如果所得到的的p包含img属性
                    Elements picture = txt.select("img");
//                    System.out.println("检测到了图片"+"\n");
                    String picUrl=oldUrl + picture.attr("src");
                    if(!(picUrl.contains("share"))){
                        JSONObject jsonObject1=new JSONObject();
                        jsonObject1.put("type","2");
                        jsonObject1.put("content",picUrl);
                        Text.add(jsonObject1);
                    }

                }

                buffer.append(txt.ownText() + "\n");
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("type","1");
                jsonObject.put("content",txt.ownText());
                Text.add(jsonObject);
            }

            News news1 = new News();
            news1.setMedia("人民网");
            news1.setNclass(tag);
            news1.setTime(time);
            news1.setTitle(title);
            news1.setText(String.valueOf(Text));
            //查找有没有type2，此处可以用查找算法
            for(int i=0;i<Text.size();i++){
                JSONObject jsonObject= (JSONObject) Text.get(i);
                if(jsonObject.get("type")=="2"){
                    news1.setImage((String) jsonObject.get("content"));
                    System.out.println("image:"+news1.getImage());
                    break;
                }
            }
            if(news1.getImage()==null){
                news1.setImage("http://www.people.com.cn/img/2020peopleindex/img/logo_share_ap.jpg");
            }
            news1.setSums(0);
            return news1;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /*** 党网时政网页专属爬虫
     * public static String urlCpc = "http://cpc.people.com.cn/"
     * ***/
    public static News GetNewsCpc(String url,String oldUrl){
        Elements news = null;
        String title = null;
        String time = null;
        StringBuffer buffer = new StringBuffer();
        JSONArray Text = new JSONArray();
        String picUrl = null;
        try {
            Document document = Jsoup.connect(url).get();

            news = document.select("div.text_c>h1");
            title = news.text();
            news = document.select("div.text_c>p.sou");
            time = news.text(); //时间
            if(time.equals("")||title.equals(""))
                return null;
            time=time.substring(0,11);
            time=time.replace('年', '.');
            time=time.replace('月', '.');
            time=time.replace('日', '.');
            System.out.println(time+"\n");

            news = document.select("div.show_text>p");
            for (Element txt : news){
                if (!txt.select("img").isEmpty()) { //如果所得到的的p包含img属性
                    Elements picture = txt.select("img");
                    picUrl= oldUrl + picture.attr("src");
                    System.out.println("pic:"+picture);
                    if(!(picUrl.contains("share"))) {
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("type", "2");
                        jsonObject1.put("content", picUrl);
                        Text.add(jsonObject1);
                        buffer.append(txt.ownText());
                    }
                }
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("type","1");
                jsonObject.put("content",txt.ownText());
                Text.add(jsonObject);

            }
            System.out.println(buffer.toString()+"\n");
            News news1 = new News();
            news1.setMedia("人民网");
            news1.setNclass("党网时政");
            news1.setTime(time);
            news1.setTitle(title);
            news1.setText(String.valueOf(Text));
            //查找有没有type2，此处可以用查找算法
            for(int i=0;i<Text.size();i++){
                JSONObject jsonObject= (JSONObject) Text.get(i);
                if(jsonObject.get("type")=="2"){
                    news1.setImage((String) jsonObject.get("content"));
                    System.out.println("image:"+news1.getImage());
                    break;
                }
            }
            if(news1.getImage()==null){
                news1.setImage("http://www.people.com.cn/img/2020peopleindex/img/logo_share_ap.jpg");
            }
            news1.setSums(0);
            return news1;

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /*** 健康生活 网页专属爬虫
     * public static String urlHealth = "http://health.people.com.cn/"
     * ***/
    public static News GetNewsHealth(String url,String oldUrl){
        Elements news = null;
        String title = null;
        String time = null;
        StringBuffer buffer = new StringBuffer();
        JSONArray Text = new JSONArray();
        String picUrl = null;

        try {
            Document document = Jsoup.connect(url).get();

            news = document.select("div.title");
            title = news.text();
            news = document.select("div.artOri");
            time = news.text();
            if(time.equals("")||title.equals(""))
                return null;
            time=time.substring(0,11);
            time=time.replace('年', '.');
            time=time.replace('月', '.');
            time=time.replace('日', '.');
            System.out.println(time+"\n");

            news = document.select("div.artDet>p");
            for (Element txt : news){
                if (!txt.select("img").isEmpty()) { //如果所得到的的p包含img属性
                    Elements picture = txt.select("img");
                    picUrl= oldUrl + picture.attr("src");
                    System.out.println("pic:"+picture);
                    if(!(picUrl.contains("share"))) {
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("type", "2");
                        jsonObject1.put("content", picUrl);
                        Text.add(jsonObject1);
                    }
                }


                JSONObject jsonObject=new JSONObject();
                jsonObject.put("type","1");
                jsonObject.put("content",txt.ownText());
                Text.add(jsonObject);
                buffer.append(txt.ownText());
            }
            System.out.println(buffer.toString()+"\n");
            News news1 = new News();
            news1.setMedia("人民网");
            news1.setNclass("健康生活");
            news1.setTime(time);
            news1.setTitle(title);
            news1.setText(String.valueOf(Text));
            //查找有没有type2，此处可以用查找算法
            for(int i=0;i<Text.size();i++){
                JSONObject jsonObject= (JSONObject) Text.get(i);
                if(jsonObject.get("type")=="2"){
                    news1.setImage((String) jsonObject.get("content"));
                    System.out.println("image:"+news1.getImage());
                    break;
                }
            }
            if(news1.getImage()==null){
                news1.setImage("http://www.people.com.cn/img/2020peopleindex/img/logo_share_ap.jpg");
            }
            news1.setSums(0);
            return news1;


        }catch (IOException e){
            e.printStackTrace();

            return null;
        }
    }

    public static List GetUrl(int type) throws IOException {
        List list = new ArrayList<>();//用list来存放地址
        String uri=UrlPath.get(type);
        URL url = new URL(uri);
        String protocol = url.getProtocol();//获取协议
        String host = url.getHost();//获取域名
        Document doc = Jsoup.connect(uri).get();//dom解析html
        Elements ele = doc.getElementsByTag("a");//获取网页中的a标签

        for (Element a : ele) {
            String href = a.attr("href");
            /**
             * a标签中有四种值，需要判断，例如：
             * 1.只有路径：/citylist.html
             * 2.含有js代码：javascript:void(0)
             * 3.网址全称：http://www.xuecheyi.com/Info/List-83.html
             * 4.没有后缀/Info
             *
             */
            String target = a.attr("class");
            String reg = "[a-zA-z]+://[^\\s]*";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(href);


            if (m.find() && target.equals("fr") || target.equals("news_box")) {//通过正则表达式匹配了第三种http://jx.xuecheyi.com/member/login/index
                list.add(href);
            } else if (href.indexOf("/") == 0) {//匹配第一四两种
                /**
                 * /login/ind
                 * 0123456789
                 * 匹配出来的地址需要在前面加上协议和域名
                 */
                list.add(protocol + "://" + host + href);

            }
        }
        return list;
    }

    public static String imageFileToBase64(String imageFilePath) {
        InputStream in = null;
        byte[] data = null;

        try {
            in = new FileInputStream(imageFilePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return Base64.getEncoder().encodeToString(data);
    }

    public static String retTag(String url){
        if (url.equals("http://finance.people.com.cn/"))
            return "经济・科技";
        else if (url.equals("http://society.people.com.cn/"))
            return "社会・法治";
        else if (url.equals("http://ent.people.com.cn/"))
            return "文旅・体育";
        else if (url.equals("http://opinion.people.com.cn/"))
            return "观点";
        else if (url.equals("http://world.people.com.cn/"))
            return "国际";
        else if (url.equals("http://military.people.com.cn/"))
            return "军事";
        else if (url.equals("http://hm.people.com.cn/"))
            return "港澳";
        else if (url.equals("http://tw.people.com.cn/"))
            return "台湾频道";
        else if (url.equals("http://edu.people.com.cn/"))
            return "教育";
        else
            return null;
    }
}