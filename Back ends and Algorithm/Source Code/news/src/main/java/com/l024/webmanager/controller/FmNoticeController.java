package com.l024.webmanager.controller;


import com.l024.webmanager.entity.FmNoticeEntity;
import com.l024.webmanager.entity.PageEntity;
import com.l024.webmanager.service.FmNoticeService;
import com.l024.webmanager.util.UserLoginToken;
import com.l024.webmanager.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(value="公司公告留言管理")
@RestController
@RequestMapping("/notice")
public class FmNoticeController implements BaseController<FmNoticeEntity>{
    @Autowired
    private FmNoticeService fmNoticeService;

    @ApiOperation(httpMethod = "POST", value = "添加留言公告", response = R.class,
            notes = "添加留言公告")
    @PostMapping("/add")
    @UserLoginToken
    @Override
    public R add(@RequestBody FmNoticeEntity fmNoticeEntity) {
        if(fmNoticeEntity!=null){
            boolean add = fmNoticeService.add(fmNoticeEntity);
            if(add){
                return R.ok("添加成功","");
            }
        }
        return R.error(500,"添加失败");
    }

    @ApiOperation(httpMethod = "GET", value = "删除留言公告", response = R.class,
            notes = "删除留言公告")
    @GetMapping("/del")
    @UserLoginToken
    @Override
    public R del(Long id) {
        if(id>0){
            boolean del = fmNoticeService.del(id);
            if(del){
                return R.ok("删除成功","");
            }
        }
        return R.error(500,"删除失败");
    }

    @ApiOperation(httpMethod = "POST", value = "更新公告留言信息", response = R.class,
            notes = "更新公告留言信息")
    @PostMapping("/update")
    @UserLoginToken
    @Override
    public R update(@RequestBody FmNoticeEntity fmNoticeEntity) {
        if(fmNoticeEntity!=null&&fmNoticeEntity.getId()>0){
            boolean update = fmNoticeService.update(fmNoticeEntity);
            if(update){
                return R.ok("更新成功","");
            }
        }
        return R.error(500,"更新失败");
    }

    @Override
    public R getAll() {
        return null;
    }

    @Override
    public R get(FmNoticeEntity fmNoticeEntity) {
        return null;
    }

    @ApiOperation(httpMethod = "POST", value = "分页获取公告留言信息", response = R.class,
            notes = "分页获取公告留言信息")
    @PostMapping("/page")
    @UserLoginToken
    public R getPages(@RequestBody Map<String,Object> map) {
        if(map!=null){
            int page = 0;
            int size = 10;
            if(map.containsKey("page")){
                page = (int)map.get("page");
            }
            if(map.containsKey("size")){
                size = (int)map.get("size");
            }
            PageEntity<FmNoticeEntity> pages = fmNoticeService.getPage(page, size);
            return R.ok("获取留言公告列表",pages);
        }
        return R.ok("获取留言公告列表","");
    }
}
