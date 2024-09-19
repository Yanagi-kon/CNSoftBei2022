package com.l024.app.appfile.service.Impl;

import china.mobile.common.demo.TtsHttpClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.l024.app.appfile.dao.NewsMapper;
import com.l024.app.appfile.entity.News;
import com.l024.app.appfile.repo.NewsRepository;
import com.l024.app.appfile.service.NewsService;
import com.l024.app.appfile.utils.FileUtil;
import com.l024.webmanager.dto.NewsDto;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static com.l024.app.appfile.utils.IatClient.SoundRec;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsMapper newsMapper;
    @Resource
    NewsRepository newsRepository;
    @Value("${path.baseUrl}")
    private String baseUrl;
    @Value("${path.url}")
    private String url;

/*
    @Override
    public String GetTtsNews(int nid) throws Exception {
        News news = newsRepository.findById(nid);
        String inputText = new String("新闻类型：" + news.getNclass() + ",新闻标题：" + news.getTitle() +
                "，发布媒体：" + news.getMedia() + ",发布时间：" + news.getTime() + ",新闻正文：" + news.getText() + "新闻阅读完毕！");
        byte[] bytes = TtsHttpClient.TtsCall(inputText);
        String filetoName = FileUtil.filetoName();
        //将byte数组转换文件
        String filename = getFileByByte(bytes, url, filetoName);
        return baseUrl + filename;
    }*/

    @Override
    public String GetTtsNews(int nid) throws Exception {
        News news = newsRepository.findById(nid);
        String text="";
        JSONArray jsonArray=JSON.parseArray(news.getText());
        //这里的查找也是可以优化的
        for(int i=0;i< jsonArray.size();i++){
            JSONObject jsonObject= (JSONObject) jsonArray.get(i);
            if(((jsonObject.get("type")).toString()).equals("1")){
                text=text+jsonObject.get("content");
            }
        }

        String inputText = new String("类型：" + news.getNclass() + ",标题：" + news.getTitle() +
                 ",时间：" + news.getTime() + ",正文：" + text + "，新闻阅读完毕！");
        byte[] bytes = null;
        try{
            bytes = TtsHttpClient.TtsCall(inputText);
        }catch (Exception e){
            return null;
        }

        //String filetoName = FileUtil.filetoName();
        //将byte数组转换文件
        String filename = getFileByByte(bytes, url, String.valueOf(nid));
        return baseUrl + filename;
    }

    public static String getFileByByte(byte[] bytes, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//看看目录存不存在
                dir.mkdirs();
            }
            String path = filePath + fileName + ".mp3";
            file = new File(path);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            //最终释放资源
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return fileName + ".mp3";
    }

    @Override
    public News GetNews(int nid) {
        News news = newsRepository.findById(nid);
        newsMapper.updateSumByNid(nid);
        return news;
    }

    @Override
    public List<News> getAllNews() {
        List<News> newsList = newsRepository.findAll();
        return newsList;
    }

    @Override
    public Map<String, Object> getNewsPage(int page, int size, String nclass) {

        PageHelper.startPage(page,size);
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("nclass",nclass);
        List<News> arr = newsMapper.getNewsList(stringStringHashMap);

        PageInfo<News> newsPageInfo = new PageInfo<>(arr);
        HashMap<String, Object> map = new HashMap<>();

        List<News> newsList=newsPageInfo.getList();

        map.put("news", newsList);
        Map<String, Object> pages = new HashMap<>();
        pages.put("total", newsPageInfo.getTotal());
        pages.put("page", page);
        pages.put("size", size);
        map.put("page", pages);
        return map;
    }

    @Override
    public Map<String, Object> getNewsPageWeb(int page, int size, String nclass) {
        PageHelper.startPage(page,size);
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("nclass",nclass);
        List<News> arr = newsMapper.getNewsListWeb(stringStringHashMap);
        PageInfo<News> newsPageInfo = new PageInfo<>(arr);
        HashMap<String, Object> map = new HashMap<>();

        List<News> newsList=newsPageInfo.getList();

        map.put("news", newsList);
        Map<String, Object> pages = new HashMap<>();
        pages.put("total", newsPageInfo.getTotal());
        pages.put("page", page);
        pages.put("size", size);
        map.put("page", pages);
        return map;
    }

    @Override
    public void delNews(int nid) {
        newsRepository.deleteAllById(nid);
    }

    @Override
    public void updateNews(int nid, String title, String nclass, String media, String time, String text) {
        newsRepository.updateNews(nid, title, nclass, media, time, text);
    }

    @Override
    public void addNews(News news) {
        String text = news.getText();
        ArrayList<Map<String, Object>> arr = new ArrayList<>();
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("type","1");
        stringObjectHashMap.put("content",text);
        arr.add(stringObjectHashMap);
        news.setText(JSON.toJSONString(arr));
        newsRepository.save(news);
    }

    @Override
    public List<News> findNewsByNclass(String nclass) {
        List<News> newsList = newsRepository.findNewsByNclass(nclass);
        return newsList;
    }

    @Override
    public List<News> findNewsByTextLike(String keyword) {
        List<News> newsList = newsRepository.findNewsByTextLike("%" + keyword + "%");
        for(int i=0;i<newsList.size();i++){
            News news=newsList.get(i);
            if(news.getTheme()==null||news.getTheme().equals("")){
                System.out.println("去掉了");
                newsList.remove(news);
            }
        }
        return newsList;
    }

    @Override
    public List<News> findNewsByTitleLike(String keyword) {
        List<News> newsList = newsRepository.findNewsByTitleLike("%" + keyword + "%");
        for(int i=0;i<newsList.size();i++){
            News news=newsList.get(i);
            if(news.getTheme()==null||news.getTheme().equals("")){
                System.out.println("去掉了");
                newsList.remove(news);
            }
        }
        return newsList;
    }

    @SneakyThrows
    @Override
    public Map<String, Object> asr(MultipartFile file) {
        String result = SoundRec(file);
        String regEx = "[\n`~!@#$%^&*()+=|{}':;'。,\\[\\].<>/?~！@#￥%……&*()——+|{}【】‘；：”“’。， 、？]\"";
        String replace = result.replaceAll(regEx, "");
        String replace1 = replace.replace("]", "").replace("。","");
        Map<String, Object> type = new HashMap<>();
        type.put("content",replace1);
        if (replace1.indexOf("电量") > -1) {
            type.put("type", "电量");
        } else if (replace1.indexOf("时间") > -1) {
            type.put("type", "时间");
        } else if (replace1.indexOf("新闻") > -1) {

            List<News> list = newsMapper.getNewsLike(replace1.replace("新闻", ""));
            type.put("type", "新闻");
            type.put("news", list);
        } else {
            type.put("type", "错误");
        }
        return type;
    }

    @SneakyThrows
    @Override
    public String GetVoice(String text) {
        byte[] bytes = TtsHttpClient.TtsCall(text);
        String filetoName = FileUtil.filetoName();
        //将byte数组转换文件
        String filename = getFileByByte(bytes, url, filetoName);
        return baseUrl + filename;
    }

    /**
     * 新闻类别总量 和新闻类别总浏览量
     *
     * @return
     */
    @Override
    public Map<String, Object> NewsByType() {
        List<NewsDto> newsDtos = newsMapper.getNewsByType();

        Map<String, Object> map = new HashMap<>();

        List<Object> listK = new ArrayList<>();
        List<Object> listV = new ArrayList<>();
        ;
        newsDtos.forEach(v -> {
            listK.add(v.getNclass());
            listV.add(v.getCount());
        });
        map.put("newsNclassByCount", listK);
        map.put("newsCount", listV);

        List<Object> k = new ArrayList<>();
        List<Object> v = new ArrayList<>();
        List<NewsDto> newsDtos1 = newsMapper.getSumByNclass();
        newsDtos1.forEach(s -> {
            k.add(s.getNclass());
            v.add(s.getSum());

        });
        map.put("newsNclassBySum", k);
        map.put("newsSum", v);

        return map;
    }

    @Override
    public List<Map<String, Object>> distribution() {
        return newsMapper.getUserDistribution();
    }
}
