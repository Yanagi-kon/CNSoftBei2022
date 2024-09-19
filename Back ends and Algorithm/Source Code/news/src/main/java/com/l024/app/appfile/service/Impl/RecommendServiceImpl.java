package com.l024.app.appfile.service.Impl;


import com.l024.app.appfile.entity.History;
import com.l024.app.appfile.entity.News;
import com.l024.app.appfile.repo.HistoryRepository;
import com.l024.app.appfile.repo.NewsRepository;
import com.l024.app.appfile.service.RecommendService;
import com.l024.app.appfile.utils.QuickSort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RecommendServiceImpl implements RecommendService {
    @Resource
    HistoryRepository historyRepository;
    @Resource
    NewsRepository newsRepository;

    @Override
    public List<News> recommend(Integer uid) {
        //推荐算法：1.计算用户对15个主题新闻的偏好 2.计算所有用户未看过的新闻与用户偏好相似度 3.推荐相似度前10的新闻

        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        String currentTime=(dateFormat.format(date)).toString();
        String year=currentTime.substring(0,4);
        String month=currentTime.substring(5,7);
        String day=currentTime.substring(8,10);
        int Year=Integer.parseInt(year);
        int Month=Integer.parseInt(month);
        int Day=Integer.parseInt(day);
        List<Double> userPreference=new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            userPreference.add(0.01);
        }
        List<List<Double>> switchPreference=new ArrayList<>();
        List<News> hasRead=new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            switchPreference.add(new ArrayList<>());
        }
        List<History> historyList=new ArrayList<>();
        if(uid!=null){
            historyList=historyRepository.findHistoryByUidAndType(uid,"1");
        }
        if(historyList.size()>0) {
            for (int i = 0; i < historyList.size(); i++) {
                History history = historyList.get(i);
                String title = history.getDetail();
                String readTime = String.valueOf(history.getCreateTime());
                String year2 = readTime.substring(0, 4);
                String month2 = readTime.substring(5, 7);
                String day2 = readTime.substring(8, 10);
                int Year2 = Integer.parseInt(year2);
                int Month2 = Integer.parseInt(month2);
                int Day2 = Integer.parseInt(day2);
                News news = newsRepository.findNewsByTitle(title);
                if (news != null) {
                    hasRead.add(news);
                    String theme = news.getTheme();
                    theme = theme.replace("[", "");
                    theme = theme.replace("]", "");
                    String[] Theme = theme.split(" ");
                    List<Double> preference = new ArrayList<>();
                    List<String> themes = new ArrayList<>();
                    for (String num : Theme) {
                        if (!num.equals("")) {
                            themes.add(num);
                        }
                    }
                    for (int j = 0; j < themes.size(); j++) {
                        //preference为单个新闻的主题概率
                        //时间差按天计算
                        preference.add(((Double.parseDouble(themes.get(j))) * (Math.exp(-0.1 * ((Year - Year2) * 365) + (Month - Month2) * 30) + (Day - Day2))));
                        (switchPreference.get(j)).add(preference.get(j));
                    }
                }

            }
            for (int i = 0; i < switchPreference.size(); i++) {
                Double prob = 0.0;
                for (int j = 0; j < (switchPreference.get(0)).size(); j++) {
                    prob += (switchPreference.get(i)).get(j);
                }
                prob = prob / (switchPreference.get(0)).size();
                userPreference.set(i, prob);
            }
        }
        //userPreference为某一用户对浏览过新闻的平均值
        System.out.println("userPreference:"+userPreference);

        //计算余弦相似度
        List<Double> similarityList=new ArrayList<>();
        Map<Double,Integer> map1=new HashMap<>();
        List<News> allNews=newsRepository.findNewsByThemeIsNotNull();

        for (int i = 0; i < allNews.size(); i++) {
            News news=allNews.get(i);
            if(!(hasRead.contains(news))) { //不包含已读新闻
                String newsTheme = news.getTheme();
                if(newsTheme==null){
                    System.out.println("主题为空");
                }

                int nid = news.getId();
                //NewsThemes
                newsTheme = newsTheme.replace("[", "");
                newsTheme = newsTheme.replace("]", "");
                String[] Theme = newsTheme.split(" ");
                List<String> NewsTheme = new ArrayList<>();
                List<Double> NewsThemes = new ArrayList<>();
                for (String num : Theme) {
                    if (!num.equals("")) {
                        NewsTheme.add(num);
                    }
                }

                for (int j = 0; j < NewsTheme.size(); j++) {
                    NewsThemes.add(Double.parseDouble(NewsTheme.get(j)));

                }

                //分子分母
                Double up = 0.0;
                Double down = 0.0;
                Double down1 = 0.0;
                Double down2 = 0.0;
                Double similarity = 0.0;

                for (int j = 0; j < userPreference.size(); j++) {
                    up = up + ((userPreference.get(j)) * (NewsThemes.get(j)));
                }
                for (int j = 0; j < userPreference.size(); j++) {
                    down1 = down1 + userPreference.get(j) * userPreference.get(j);
                    down2 = down2 + NewsThemes.get(j) * NewsThemes.get(j);
                }
                down = Math.sqrt(down1) * Math.sqrt(down2);
                similarity = up / down;
                similarityList.add(similarity);
                map1.put(similarity, nid);   //根据相似度来找nid
            }
        }

        List<Double> list=new ArrayList<>(map1.keySet());
        //计算完成，快排选最大的前10个
        List<Double> OrderedList= QuickSort.quick_sort(list,0,list.size());
        List<News> recommendList=new ArrayList<>();
        for (int i = OrderedList.size()-1; i > OrderedList.size()-11; i--) {
            System.out.println("userPreference:"+userPreference);
            System.out.println("his:"+historyList.size());
            System.out.println("map1:"+map1);
            System.out.println(OrderedList.size());
            recommendList.add(newsRepository.findNewsById(map1.get(OrderedList.get(i))));
        }

        return recommendList;
    }
}
