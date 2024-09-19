package com.l024.app.appfile.backend.controller;

import com.l024.app.appfile.backend.service.UserBackendService;
import com.l024.app.appfile.entity.resp.RestBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Auther: Mr.yang
 * @Date: 2022/06/05/6:04
 * @Description: 描述：用户后台管理
 */
@Api("用户后台管理")
@RestController
@RequestMapping("/api/backend/user")
public class UserBackendController {
    private final UserBackendService userBackendService;

    public UserBackendController(UserBackendService userBackendService) {
        this.userBackendService = userBackendService;
    }

    @PostMapping("/getUser")
    @ApiOperation(value = "查看用户列表")
    public RestBean<Map<String,Object>> getUserList(@RequestBody Map<String,Integer> map ){
      Map<String,Object>  userList =  userBackendService.getUserList(map.get("page"),map.get("size"));
        return new RestBean<>(200,"ok",userList);
    }

    @PostMapping("/history")
    @ApiOperation(value = "查看用户操作记录")
   public RestBean<Map<String,Object>> getHistory(Integer page,Integer size,Integer uid){
        Map<String,Object> history =  userBackendService.getHistory(page,size,uid);
        return  new RestBean<>(200,"ok",history);
    }
    @PostMapping("/webHistory")
    @ApiOperation(value = "后台查看用户操作记录")
    public RestBean<Map<String,Object>> webHistory(@RequestBody Map<String,String> param){
        Map<String,Object> history =  userBackendService.getHistory(Integer.parseInt(param.get("page")),Integer.parseInt(param.get("size")),Integer.parseInt(param.get("uid")));
        return  new RestBean<>(200,"ok",history);
    }



}
