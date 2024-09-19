package com.l024.webmanager.controller;

import com.l024.webmanager.entity.FmCompanyEntity;
import com.l024.webmanager.service.FmCompanyService;
import com.l024.webmanager.util.LogUtil;
import com.l024.webmanager.util.UserLoginToken;
import com.l024.webmanager.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value="公司信息管理")
@RestController
@RequestMapping("/company")
public class FmCompanyController {
    @Autowired
    private FmCompanyService fmCompanyService;
    @ApiOperation(httpMethod = "GET", value = "获取当前公司信息", response = R.class,
            notes = "公司详情")
    @GetMapping("/info")
    @UserLoginToken
    public R getInfo() {
        List<FmCompanyEntity> all = fmCompanyService.getAll();
        LogUtil.i("当前用户信息：",all);
        if(all!=null&&all.size()>0){
            return R.ok("获取用户详情",all.get(0));
        }
        return R.ok("获取用户详情","");
    }

}
