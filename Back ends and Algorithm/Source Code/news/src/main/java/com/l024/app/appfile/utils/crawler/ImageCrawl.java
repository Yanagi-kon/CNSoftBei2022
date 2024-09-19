package com.l024.app.appfile.utils.crawler;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageCrawl {

//    public static String url = "https://www.nipic.com/topic/show_27068_1.html";

    public static String url = "http://tw.people.com.cn/"; //人民网 经济科技栏目


    public static void main(String[] args) throws IOException {

        //获取所需的所有网页：
        List list = GetUrl(url);
        Iterator it =list.iterator();
        while (it.hasNext())
        {
            GetNews(it.next().toString(),url);
//            System.out.println(it.next().toString());
        }

//        GetNews("http://world.people.com.cn/n1/2022/0523/c1002-32428387.html","http://world.people.com.cn/");

//        ————————————————————————————————————————————————
    }



    public static void GetNews(String url,String oldUrl){
        Elements news = null;
        Elements picture = null;
        int picNumber = 0;
        String tag = null;
        String title = null;
        String time = null;
        String text = null;
        StringBuffer buffer = new StringBuffer();
        List<String> picName = new ArrayList<String>();
        try {
            Document document = Jsoup.connect(url).get();

//            news = document.select("div#rwb_navpath.layout.route.cf>a");
            tag = retTag(oldUrl);

            news = document.select("div.col.col-1.fl>h1");
            title = news.text();
//            System.out.println("文章标题:"+news.text());

            news = document.select("div.col-1-1.fl");
            time = news.text();
            buffer.append(time+"\n");
            System.out.println("这是时间："+time);


            picture = document.select("div.rm_txt_con.cf>p>img");
            picNumber = picture.size();
            System.out.println(picture+" "+picNumber);
            for (Element pic:picture){
                if (pic.hasAttr("desc")){
                    picName.add(pic.attr("desc"));
                    picNumber--;
                }
            }

            news = document.select("div.rm_txt_con.cf>p");  //人民网几乎文本都在这里
//            System.out.println("正文内容："+"\t");
            for (Element txt : news) {
                if (!txt.ownText().isEmpty()) {
                    if (!txt.select("img").isEmpty()) {
                        Element midElement = news.next().first();
                        if (!picName.contains(midElement.text()) && picNumber > 0) {
                            picName.add(midElement.text());
                        }
                    }
                    buffer.append(txt.ownText() + "\n");
//                System.out.println(txt.ownText());
                }
            }
            text = buffer.toString();

            picNumber = picture.size(); //重新对picNumber赋值

        }catch (IOException e){
            e.printStackTrace();
        }

        try {
            File file = new File("C:\\Users\\Yanagi\\Desktop\\新闻\\"+tag+"\\"+title+".txt");
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(writer);
            out.write(text);
            out.flush();
            out.close();

//            System.out.println("图片链接:"+picture.attr("src"));
            while (picNumber>0) {
                for (Element pic : picture) {

                    Connection.Response response = Jsoup.connect(oldUrl + pic.attr("src"))
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Safari/537.36")
                            .ignoreContentType(true).execute();
                    ByteArrayInputStream stream = new ByteArrayInputStream(response.bodyAsBytes());

                    System.out.println(oldUrl + picture.attr("src"));

                    List<String> list = picName;
                    Iterator<String> it = list.iterator();
                    if (!it.hasNext()) {
                        FileUtils.copyInputStreamToFile(stream, new File("C:\\Users\\Yanagi\\Desktop\\新闻\\" + picNumber + ".jpg"));
                    } else
                        FileUtils.copyInputStreamToFile(stream, new File("C:\\Users\\Yanagi\\Desktop\\新闻\\" +tag+"\\图片\\"+ picNumber + it.next().toString() + ".jpg"));
                    picNumber--;
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
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

    /**
     * 得到网站的所有网址
     * @param uri
     * @return
     * @throws IOException
     */
    public static List GetUrl(String uri) throws IOException {
            List list=new ArrayList<>();//用list来存放地址
            URL url=new URL(uri);
            String protocol=url.getProtocol();//获取协议
            String host=url.getHost();//获取域名
            Document doc=Jsoup.connect(uri).get();//dom解析html
            Elements ele=doc.getElementsByTag("a");//获取网页中的a标签

            for (Element a:ele){
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
                String reg="[a-zA-z]+://[^\\s]*";
                Pattern p=Pattern.compile(reg);
                Matcher m=p.matcher(href);


                if(m.find()&&target.equals("fr")||target.equals("news_box")){//通过正则表达式匹配了第三种http://jx.xuecheyi.com/member/login/index
                    list.add(href);
                }else if(href.indexOf("/")==0){//匹配第一四两种
                    /**
                     * /login/ind
                     * 0123456789
                     * 匹配出来的地址需要在前面加上协议和域名
                     */
                    list.add(protocol+"://"+host+href);

                }
            }
            return list;
        }
}
