package com.l024.app.appfile.dao;

import com.l024.app.appfile.entity.News;
import com.l024.app.appfile.repo.NewsRepository;
import com.l024.webmanager.dto.NewsDto;
import com.l024.webmanager.dto.UserDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Mr.yang
 * @Date: 2022/06/05/7:54
 * @Description: 描述：
 */
@Repository
public interface NewsMapper {

    void updateUserInfo(UserDto user);

    void updateSumByNid(int nid);

    List<News> getNews(News news);

    List<NewsDto> getNewsByType();

    List<NewsDto> getSumByNclass();

    List<News> getNewsLike(String s);

    List<News> lit(String s);

    List<News> getNewsList(HashMap<String, String> map);
    List<News> getNewsListWeb(HashMap<String, String> map);

    List<Map<String, Object>> getUserDistribution();

}
