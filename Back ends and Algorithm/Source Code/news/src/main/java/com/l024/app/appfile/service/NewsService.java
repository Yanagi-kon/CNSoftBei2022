package com.l024.app.appfile.service;
import com.l024.app.appfile.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface NewsService {
    String GetTtsNews(int nid) throws Exception;
    News GetNews(int nid);
    List<News> getAllNews();
    Map<String,Object> getNewsPage(int page, int size,String nclass);
    Map<String,Object> getNewsPageWeb(int page, int size,String nclass);
    void delNews(int nid);
    void updateNews(int nid, String title,String nclass,String media,String time,String text);
    void addNews(News news);
    List<News> findNewsByNclass(String nclass);
    List<News> findNewsByTextLike(String keyword);
    List<News> findNewsByTitleLike(String keyword);

    Map<String,Object> asr(MultipartFile file);

    String GetVoice(String text);

    Map<String, Object> NewsByType();

    List<Map<String,Object>> distribution();

}
