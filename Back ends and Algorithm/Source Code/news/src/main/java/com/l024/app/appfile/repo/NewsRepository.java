package com.l024.app.appfile.repo;


import com.l024.app.appfile.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News,Integer> {
    News findById(int nid); //根据nid查找单个新闻

    List<News> findNewsByNclass(String nclass);  //查找某一类的新闻

    List<News> findNewsByTextLike(String keyword); //根据关键词查找新闻
    List<News> findNewsByTitleLike(String keyword); //根据关键词查找新闻
    @Transactional
    @Modifying
    void deleteAllById(int nid);

    @Transactional
    @Modifying     //修改新闻信息
    @Query("update News set title = ?2 ,nclass=?3 ,media=?4,time=?5,text=?6 where id = ?1")
    void updateNews(int nid, String title,String nclass,String media,String time,String text);



   @Query("select s from News as s")
    Page<News> selectall(Pageable of);
    @Query("select s from News as s where s.nclass=?1")
    List<News> getByNclass(String nclass);

    @Modifying
    @Query("update News set theme = ?2 where id = ?1")
    void updateTheme(int nid, String theme);

    News findNewsById(Integer nid);

    News findNewsByTitle(String title);
    List<News> findNewsByThemeEquals(String str);

    @Query(value="select * from News where theme is not null",nativeQuery = true)
    List<News> findNewsByThemeIsNotNull();
}
