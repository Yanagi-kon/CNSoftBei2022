package com.l024.app.appfile.service.Impl;

import com.l024.app.appfile.entity.News;
import com.l024.app.appfile.repo.NewsRepository;
import com.l024.app.appfile.service.NewsCrawlerService;
import org.junit.Test;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.l024.app.appfile.utils.crawler.NewsCrawl.*;

@Service
public class NewsCrawlerServiceImpl implements NewsCrawlerService {

    @Resource
    NewsRepository repository;

    @Test
    @Override
    public List<News> addCrawlerNews(int type) throws IOException {
        List<News> newsList=new ArrayList<>();
        if(type<0||type>10)  type=0;
        if(type<9)
        {
            List list = GetUrl(type);
            Iterator it = list.iterator();
            while (it.hasNext()) {
                News news1=GetNews(it.next().toString(), type);
                if(news1!=null)
                try {
                    newsList.add(news1);
                }catch (Exception e){
                    System.out.println("新闻重复！");
                }
            }
            return newsList;
        }

        if(type==9){
            List list2 = GetUrl(9);
            Iterator it2 = list2.iterator();
            while (it2.hasNext()) {
                News news2=GetNewsCpc(it2.next().toString(), "http://cpc.people.com.cn/");
                if(news2!=null)
                try {
                    newsList.add(news2);
                }catch (Exception e){
                    System.out.println("新闻重复！");
                }
            }
            return newsList;
        }
        if(type==10){
            List list3 = GetUrl(10);
            Iterator it3 = list3.iterator();
            while (it3.hasNext()) {
                News news3=GetNewsHealth(it3.next().toString(), "http://health.people.com.cn/");
                if(news3!=null)
                {
                    try {
                        newsList.add(news3);
                    }catch (Exception e){
                        System.out.println("新闻重复！");
                    }
                }
            }
            return newsList;
        }
        return null;

    }
}
