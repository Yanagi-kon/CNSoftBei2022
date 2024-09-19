package com.l024.app.appfile.service;

import com.l024.app.appfile.entity.News;

import java.util.List;

public interface RecommendService {
    List<News> recommend(Integer uid);
}
