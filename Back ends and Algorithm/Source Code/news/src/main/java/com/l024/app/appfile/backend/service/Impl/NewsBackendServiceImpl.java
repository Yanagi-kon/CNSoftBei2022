package com.l024.app.appfile.backend.service.Impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.l024.app.appfile.backend.service.NewsBackendService;
import com.l024.app.appfile.dao.NewsMapper;
import com.l024.app.appfile.entity.News;
import com.l024.app.appfile.repo.NewsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class NewsBackendServiceImpl implements NewsBackendService {


    private final NewsRepository newsRepository;

    private final NewsMapper mapper;

    @Autowired
    private NewsMapper newsMapper;

    public NewsBackendServiceImpl(NewsRepository newsRepository, NewsMapper mapper) {
        this.newsRepository = newsRepository;
        this.mapper = mapper;
    }

    @Override
    public Map<String,Object> newsList(Map<String,Object> map) {
        String page = map.get("page").toString();
        String size = map.get("size").toString();

        News news = new News();
        news.setNclass(map.get("nclass").toString());
        news.setTitle(map.get("keyword").toString());
        PageHelper.startPage(Integer.parseInt(page),Integer.parseInt(size));
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("nclass",map.get("nclass").toString());
        List<News> arr = newsMapper.getNewsListWeb(stringStringHashMap);
        PageInfo<News> newsPageInfo = new PageInfo<>(arr);
        Map<String,Object> pagemap = new HashMap<>();

        pagemap.put("news",newsPageInfo.getList());
        pagemap.put("total",newsPageInfo.getTotal());
        pagemap.put("content",newsPageInfo.getTotal());
        pagemap.put("page",page);
        pagemap.put("size",size);

        return pagemap;
    }
}
