package com.l024.app.appfile.service;

import com.l024.app.appfile.entity.News;

import java.io.IOException;
import java.util.List;

public interface NewsCrawlerService {
    List<News> addCrawlerNews(int type) throws IOException;
}
