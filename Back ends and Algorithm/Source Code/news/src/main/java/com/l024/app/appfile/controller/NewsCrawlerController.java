package com.l024.app.appfile.controller;

import cn.hutool.poi.excel.ExcelPicUtil;
import com.l024.app.appfile.entity.News;
import com.l024.app.appfile.repo.NewsRepository;
import com.l024.app.appfile.service.ExportNewsService;
import com.l024.app.appfile.service.NewsCrawlerService;
import com.l024.app.appfile.service.UpdateThemeService;
import com.l024.app.appfile.utils.ExcelRead;
import com.l024.app.appfile.utils.PyUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/crawl")
public class NewsCrawlerController {
    @Resource
    NewsCrawlerService newsService;
    @Resource
    ExportNewsService exportNewsService;
    @Resource
    UpdateThemeService updateThemeService;
    @Resource
    NewsRepository newsRepository;

    @RequestMapping("/addNews")
    public String GetCrawlNews(int type) throws IOException {
        List<News> newsList=new ArrayList<>();
        //type为0-10 爬取某一类
        if(type!=11) {
            newsList=newsService.addCrawlerNews(type);
        }

        //type为11，11类全爬
        if(type==11){
            for(int i=0;i<11;i++){
                newsList.addAll(newsService.addCrawlerNews(i));
            }
        }
        int size=0;
        for (int i = 0; i < newsList.size(); i++) {
            News news=newsList.get(i);
            try {
                size++;
                newsRepository.save(news);
            }catch(Exception e){
                System.out.println("新闻重复！");
                size--;
            }
        }

        return "success!The number of news:"+size;
    }


    //爬虫爬了新闻，完成后调用导出文件接口，导出后调用python，python完成后,调用添加进数据库，加完后调用转音频
    @RequestMapping("/auto")
    public String AutoCrawlNews() throws Exception {
        List<News> newsList = new ArrayList<>();
        //11类全爬
        for (int i = 0; i < 11; i++) {
            List<News> list = newsService.addCrawlerNews(i);
            newsList.addAll(list);
        }
        if (newsList.size() > 0) {
            System.out.println("爬取新闻成功！");
            exportNewsService.ExportNews(newsList); //导出data.xlsx
            System.out.println("导出data.xlsx成功！");
            PyUtil.GetPyFile("python D:\\PycharmProject\\LDA_Code\\LDA_test.py");  //执行pyhton，生成newdata.xlsx
            System.out.println("执行pyhton，生成newdata.xlsx成功！");
            List<Map<String, String>> themeList = ExcelRead.ReadExcel(
                    new File("D:\\PycharmProject\\LDA_Code\\testResult\\newdata.xlsx"));
            updateThemeService.UpdateThemeAndSound(themeList,newsList);
            System.out.println("更新主题和新闻音频成功！");

            return "更新主题和新闻音频成功！";
        }
        return null;
    }



}
