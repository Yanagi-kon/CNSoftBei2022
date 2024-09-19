package com.l024.app.appfile.controller;

import com.l024.app.appfile.entity.News;
import com.l024.app.appfile.entity.resp.RestBean;
import com.l024.app.appfile.repo.NewsRepository;
import com.l024.app.appfile.service.ExportNewsService;
import com.l024.app.appfile.service.NewsService;
import com.l024.app.appfile.service.RecommendService;
import com.l024.app.appfile.service.UpdateThemeService;
import com.l024.app.appfile.utils.ExcelRead;
import com.l024.app.appfile.utils.SoundFileSearch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
public class NewsThemeController {

    //推荐接口，参数为uid，举例：http://localhost:9999/api/news/recommend?uid=6
    @Resource
    RecommendService recommendService;
    @RequestMapping("/recommend")
    public RestBean<List<News>> recommend(String uid) throws Exception {
        return new RestBean<>(200,"success",recommendService.recommend(Integer.parseInt(uid)));
    }

    @Resource
    NewsRepository newsRepository;
    @Resource
    NewsService newsService;
    @RequestMapping("/TTS")
    public String Tts() throws Exception {
        List<News> newsList=newsRepository.findAll();
        for (int i = 0; i < newsList.size(); i++) {
            if((newsList.get(i)).getTheme()==null||(newsList.get(i)).getTheme().equals("")){
                newsList.remove(i);
            }
        }
        for (int i = 0; i < newsList.size(); i++) {
            News news=newsList.get(i);
            String nid= String.valueOf(news.getId());
            if(SoundFileSearch.SoundSearch(nid)==false){
                //没有音频，开始合成
                newsService.GetTtsNews(Integer.parseInt(nid));
                System.out.println(i+"/"+newsList.size()+"  "+nid+"合成完成");
            }
        }
        return "success";
    }
}
