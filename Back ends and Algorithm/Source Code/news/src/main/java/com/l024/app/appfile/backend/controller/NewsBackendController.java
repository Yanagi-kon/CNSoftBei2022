package com.l024.app.appfile.backend.controller;


import com.l024.app.appfile.backend.service.NewsBackendService;
import com.l024.app.appfile.entity.News;
import com.l024.app.appfile.entity.resp.RestBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Api("新闻后台管理")
@RequestMapping("/api/backend/news")
@RestController
public class NewsBackendController {
     @Autowired
     private NewsBackendService newsBackendService;
    @ApiOperation("新闻列表")
    @PostMapping("/newsList")
    public RestBean<Map<String,Object>> newsList(@RequestBody Map<String,Object> news){

       Map<String,Object> map = newsBackendService.newsList(news);
        return  new RestBean<>(200,"ok",map);
    }

}
