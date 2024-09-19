package com.l024.app.appfile.service;

import com.l024.app.appfile.entity.News;

import java.util.List;
import java.util.Map;

public interface UpdateThemeService {
    void UpdateThemeAndSound(List<Map<String,String>> textList,List<News> newsList) throws Exception;

}
