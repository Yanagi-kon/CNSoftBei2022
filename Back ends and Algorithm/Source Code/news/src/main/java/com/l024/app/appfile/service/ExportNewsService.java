package com.l024.app.appfile.service;

import com.l024.app.appfile.entity.News;

import java.io.IOException;
import java.util.List;

public interface ExportNewsService {
    public void ExportNews(List<News> newsList) throws IOException;
}
