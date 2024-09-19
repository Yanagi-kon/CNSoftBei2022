package com.l024.app.appfile.controller;

import com.l024.app.appfile.entity.History;
import com.l024.app.appfile.entity.resp.RestBean;
import com.l024.app.appfile.service.HistoryService;
import com.l024.webmanager.exception.RRException;
import com.l024.webmanager.util.ObjUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: Mr.yang
 * @Date: 2022/06/05/5:22
 * @Description: 描述：
 */
@RestController
@RequestMapping("/api/history")
@Api("历史记录控制器")
public class HistoryController {
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @ApiOperation("保存历史操作记录")
    @PostMapping("/saveHistory")
    public RestBean<String> saveHistory(History history){
     if (ObjUtils.isNotNull(history)){
         throw new RRException("参数不能为null");
     }
        historyService.saveHistory(history);
  return  new RestBean<String>(200,"ok");
    }
}
