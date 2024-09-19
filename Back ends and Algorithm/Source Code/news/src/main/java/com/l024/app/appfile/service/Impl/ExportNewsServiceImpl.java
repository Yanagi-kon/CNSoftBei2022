package com.l024.app.appfile.service.Impl;

import com.l024.app.appfile.entity.News;
import com.l024.app.appfile.repo.NewsRepository;
import com.l024.app.appfile.service.ExportNewsService;
import com.l024.app.appfile.utils.ExportExcel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExportNewsServiceImpl implements ExportNewsService {
    @Resource
    NewsRepository newsRepository;

    @Override
    public void ExportNews(List<News> newsList) throws IOException {
        //创建excel表头
        List<String> column = new ArrayList<>();
        column.add("image");
        column.add("title");
        column.add("nclass");
        column.add("media");
        column.add("time");
        column.add("text");
        column.add("sums");

        //表头对应的数据
        List<Map<String,Object>> data = new ArrayList<>();
        //List<News> newsList=newsRepository.findNewsByThemeEquals(null);
        //遍历获取到的需要导出的数据，k要和表头一样
        for (int i = 0; i < newsList.size(); i++) {
            Map<String,Object> dataMap = new HashMap<>();
            News news=newsList.get(i);
            dataMap.put("image",news.getImage());
            dataMap.put("title",news.getTitle());
            dataMap.put("nclass",news.getNclass());
            dataMap.put("media",news.getMedia());
            dataMap.put("time",news.getTime());
            dataMap.put("text",news.getText());
            dataMap.put("sums",news.getSums());

            data.add(dataMap);
        }

        //调用导出工具类
        ExportExcel.exportExcel("data.xlsx",column,data);
        try {
            Thread.sleep( 5000 );
        } catch (Exception e){
        }
        System.out.println("已导出");

    }
}
