package com.l024.app.appfile.service.Impl;

import china.mobile.common.demo.TtsHttpClient;
import com.l024.app.appfile.entity.News;
import com.l024.app.appfile.repo.NewsRepository;
import com.l024.app.appfile.service.NewsService;
import com.l024.app.appfile.service.UpdateThemeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UpdateThemeServiceImpl implements UpdateThemeService {
    @Resource
    NewsRepository newsRepository;
    @Resource
    NewsService newsService;

    //参数是读取的<title,theme> map list  和  爬下来的新闻list
    @Override
    public void UpdateThemeAndSound(List<Map<String, String>> textList,List<News> newsList) throws Exception {
        int count=0;
        //从1开始
        for(int i=1;i<textList.size();i++){
            String str= String.valueOf((textList.get(i)).keySet());

            str=str.replace("[","");
            str=str.replace("]","");
            for (int j = 0; j < newsList.size(); j++) {
                if(str.equals((newsList.get(j)).getTitle())){
                    System.out.println("相等");
                    String theme=(textList.get(i)).get(str);
                    (newsList.get(j)).setTheme(theme);
                    if(theme!=null&&(newsList.get(j))!=null){
                        try{
                            count++;
                            newsRepository.save(newsList.get(j));
                        }catch(Exception e){
                            System.out.println("新闻重复！");
                            count--;
                            break;
                        }

                    }
                    News news=newsRepository.findNewsByTitle(str);
                    newsService.GetTtsNews(news.getId());
                    System.out.println(news.getId()+" 生成音频！");
                    break;
                }


            }
            System.out.println("存入新闻"+count+"条！");

        }

    }


}



