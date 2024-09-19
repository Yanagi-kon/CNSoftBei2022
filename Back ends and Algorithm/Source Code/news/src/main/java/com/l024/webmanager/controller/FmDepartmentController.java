package com.l024.webmanager.controller;

import com.l024.webmanager.entity.FmDepartmentEntity;
import com.l024.webmanager.entity.PageEntity;
import com.l024.webmanager.service.FmDepartmentService;
import com.l024.webmanager.util.LogUtil;
import com.l024.webmanager.util.UserLoginToken;
import com.l024.webmanager.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Api(value="公司部门管理")
@RestController
@RequestMapping("/department")
public class FmDepartmentController implements BaseController<FmDepartmentEntity> {
    @Autowired
    private FmDepartmentService fmDepartmentService;

    @ApiOperation(httpMethod = "POST", value = "添加部门", response = R.class,
            notes = "添加部门信息")
    @PostMapping("/add")
    @UserLoginToken
    @Override
    public R add(@RequestBody FmDepartmentEntity fmDepartmentEntity) {
        LogUtil.i("添加的部门",fmDepartmentEntity);
        if(fmDepartmentEntity!=null){
            boolean isExits = fmDepartmentService.getDepartmentByName(fmDepartmentEntity.getName());
            if(!isExits){
                boolean add = fmDepartmentService.add(fmDepartmentEntity);
                if(add){
                    return R.ok("添加成功","");
                }
            }else{
                return R.error(500,"添加失败,该部门已经存在");
            }

        }
        return R.error(500,"添加失败");
    }

    @ApiOperation(httpMethod = "POST", value = "删除部门信息", response = R.class,
            notes = "删除部门信息")
    @GetMapping("/del")
    @UserLoginToken
    @Override
    public R del(Long id) {
        if(id>0){
            boolean del = fmDepartmentService.del(id);
            if(del){
                return R.ok("删除成功","");
            }
        }
        return R.error(500,"删除失败");
    }

    @ApiOperation(httpMethod = "POST", value = "更新部门信息", response = R.class,
            notes = "更新部门信息")
    @PostMapping("/update")
    @UserLoginToken
    @Override
    public R update(@RequestBody FmDepartmentEntity fmDepartmentEntity) {
        if(fmDepartmentEntity!=null&&fmDepartmentEntity.getId()>0){
            boolean update = fmDepartmentService.update(fmDepartmentEntity);
            if(update){
                return R.ok("更新部门信息成功","");
            }
        }
        return R.error(500,"更新失败");
    }

    @Override
    public R getAll() {
        return null;
    }

    @Override
    public R get(FmDepartmentEntity fmDepartmentEntity) {
        return null;
    }

    @ApiOperation(httpMethod = "POST", value = "分页获取部门信息", response = R.class,
            notes = "部门列表")
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
            PageEntity<FmDepartmentEntity> pages = fmDepartmentService.getPages(page, size);
            return R.ok("获取客户列表",pages);
        }
        return R.ok("获取客户列表","");
    }

    @ApiOperation(httpMethod = "GET", value = "获取所有部门", response = R.class,
            notes = "获取所有部门")
    @GetMapping("/names")
    @UserLoginToken
    public R getNames() {
        List<Map<String, Object>> names = fmDepartmentService.getNames();
        if(names!=null&&names.size()>0){
            return R.ok("获取部门名称",names);
        }
        return R.error(500,"获取失败");
    }
}
