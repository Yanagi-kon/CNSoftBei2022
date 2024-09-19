package com.l024.app.appfile.controller;


import com.l024.app.appfile.entity.History;
import com.l024.app.appfile.entity.News;
import com.l024.app.appfile.entity.resp.RestBean;
import com.l024.app.appfile.service.HistoryService;
import com.l024.app.appfile.service.NewsService;
import com.l024.webmanager.exception.RRException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/news")
public class NewsApiController {
    @Resource
    NewsService newsService;
    @Resource
    HistoryService historyService;

    @Value("${path.baseUrl}")
    private String baseUrl;

    @Value("${path.url}")
    private String url;

    //新闻转语音，返回字符数组
    @RequestMapping("/ttsNews")
    public RestBean<String> GetNewsAudio(int nid) throws Exception {
        String address="http://36.138.201.130:9999/file/"+nid+".mp3";
        return new RestBean<>(200, "请求成功！", address);
    }

    //文字转语音
    @PostMapping("/ttes")
    public RestBean<String> GetVoice(String text) {
        if (text == null || text.equals("")) {
            throw new RRException("当前没有可读文件");
        }
        String path = newsService.GetVoice(text);
        return new RestBean<String>(200, "ok", path);
    }

    //id查找单个新闻
    @RequestMapping("/getNews")
    public RestBean<News> GetNews(int nid, int uid) {
        if (ObjectUtils.isEmpty(uid)) {
            throw new RRException("用户参数不能为空!");
        }
        News news = newsService.GetNews(nid);
        //将此记录插入到历史记录
        History history = new History();
        history.setUid(uid);
        history.setTypeName("新闻浏览");
        history.setType(String.valueOf(1));
        history.setCreateTime(new Date());
        history.setDetail(news.getTitle());
        historyService.saveHistory(history);
        return new RestBean<News>(200, "请求成功！", news);
    }

    //所有新闻
    @RequestMapping("/getAllNews")
    public RestBean<List<News>> GetAllNews() {
        List<News> newsList = newsService.getAllNews();
        return new RestBean<>(200, "请求成功！", newsList);
    }

    //分页查找新闻
    @RequestMapping("/getNewsPage")
    public RestBean<Map<String, Object>> GetNewsPage(int page, int size, String nclass) {
        Map<String, Object> newsList = newsService.getNewsPage(page, size, nclass);

        return new RestBean<>(200, "请求成功！", newsList);
    }
    @RequestMapping("/getNewsPageWeb")
    public RestBean<Map<String, Object>> GetNewsPageWeb(int page, int size, String nclass) {
        Map<String, Object> newsList = newsService.getNewsPageWeb(page, size, nclass);
        return new RestBean<>(200, "请求成功！", newsList);
    }

    //根据id删除新闻
    @RequestMapping("/delNews")
    public RestBean<Void> DelNews(@RequestBody Map<String, Integer> nid) {

        newsService.delNews(nid.get("nid"));
        return new RestBean<>(200, "删除成功！");
    }
/*
    //修改新闻信息，参数可以不完全，默认为空字符
    @RequestMapping("/updateNews")
    public RestBean<Void> UpdateNews(@RequestBody Map<String, String> map) {
        newsService.updateNews(Integer.parseInt(map.get("id")), map.get("title"), map.get("nclass"), map.get("media"), map.get("time"), map.get("text"));
        return new RestBean<>(200, "修改成功！");
    }*/
    @RequestMapping("/updateNews")
    public RestBean<Void> UpdateNews(@RequestBody Map<String, String> map) {
    newsService.updateNews(Integer.parseInt(map.get("id")), map.get("title"), map.get("nclass"), map.get("media"), (map.get("time")).substring(0,10), map.get("text"));
    return new RestBean<>(200, "修改成功！");
    }

    //增加新闻
    @RequestMapping("/addNews")
    public RestBean<Void> AddNews(@RequestBody News news) {

        newsService.addNews(news);
        return new RestBean<>(200, "添加成功！");
    }

    //根据类别查找新闻
    @RequestMapping("/findNewsByClass")
    public RestBean<List<News>> GetNewsByClass(String nclass) {
        List<News> newsList = newsService.findNewsByNclass(nclass);
        return new RestBean<>(200, "请求成功！", newsList);
    }

    //根据输入词查找新闻
    @RequestMapping("/searchNews")
    public RestBean<List<News>> SearchNews(String keywords) {
        List<News> newsList1 = newsService.findNewsByTextLike(keywords);
        List<News> newsList2 = newsService.findNewsByTitleLike(keywords);
        newsList1.removeAll(newsList2);  //去重
        newsList1.addAll(newsList2);    //相加
        return new RestBean<>(200, "请求成功！", newsList1);
    }

    @RequestMapping("/asr")
    public RestBean<Map<String, Object>> asr(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "uid") String uid) {
        String originalFilename = file.getOriginalFilename();
//        if (originalFilename.indexOf(".wav") == -1) {
//            throw new RRException("仅支持wav文件");
//        }

        Map<String, Object> news = newsService.asr(file);
        History history = new History();
        history.setType(String.valueOf(2));
        history.setDetail(String.valueOf(news.get("content")));
        history.setCreateTime(new Date());
        history.setUid(Integer.valueOf(uid));
        history.setTypeName("语音指令");
        historyService.saveHistory(history);

        return new RestBean<>(200, "ok", news);
    }


    @PostMapping("/newsByType")
    @ApiOperation("新闻类别总量")
    public RestBean<Map<String, Object>> NewsByType() {
        Map<String, Object> map = newsService.NewsByType();
        return new RestBean<>(200, "ok", map);
    }

    @PostMapping("/distribution")
    @ApiOperation("人数分布图")
    public RestBean<List<Map<String, Object>>> distribution() {
        List<Map<String, Object>> map = newsService.distribution();
        return new RestBean<>(200, "ok", map);
    }
    @PostMapping("/file")
    public RestBean<String> file(@RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //重新生成文件名
        fileName = UUID.randomUUID() + suffixName;
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("上传图片不能大于5M");
        }
        //指定本地文件夹存储图片
        File fileLocal = new File(url + fileName);
        file.transferTo(fileLocal);
        return new RestBean<>(200, "ok", baseUrl + fileName);
    }

}
