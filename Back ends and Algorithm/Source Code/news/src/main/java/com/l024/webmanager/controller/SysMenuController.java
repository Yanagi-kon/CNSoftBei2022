package com.l024.webmanager.controller;

import com.l024.webmanager.service.SysMenuService;
import com.l024.webmanager.util.UserLoginToken;
import com.l024.webmanager.vo.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value="系统菜单")
@RestController
@RequestMapping("/menu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;
    @GetMapping("/menus")
    @UserLoginToken
    public R getMenus(){
        return R.ok("获取菜单",sysMenuService.getAll());
    }
}
