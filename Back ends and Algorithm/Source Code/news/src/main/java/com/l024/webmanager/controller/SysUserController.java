package com.l024.webmanager.controller;

import com.l024.webmanager.entity.PageEntity;
import com.l024.webmanager.entity.SysUserEntity;
import com.l024.webmanager.service.SysUserService;
import com.l024.webmanager.service.TokenService;
import com.l024.webmanager.util.LogUtil;
import com.l024.webmanager.util.StringUtil;
import com.l024.webmanager.util.TokenUtil;
import com.l024.webmanager.util.UserLoginToken;
import com.l024.webmanager.vo.R;
import com.l024.webmanager.vo.SpringBeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(value="系统用户")
@RestController
@RequestMapping("/user")
public class SysUserController implements BaseController<SysUserEntity>{
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    TokenService tokenService;

    @ApiOperation(httpMethod = "POST", value = "系统用户添加", response = R.class,
            notes = "系统用户添加 ")
    @PostMapping("/add")
    @Override
    public R add(@RequestBody SysUserEntity sysUserEntity) {
        if(sysUserEntity!=null&&!StringUtil.isEmpty(sysUserEntity.getPhone())){
            //先查询用户是否存在
            SysUserEntity userByPhone = sysUserService.findUserByPhone(sysUserEntity.getPhone());
            if(userByPhone!=null){
                return R.error(500,"该用户手机号已经注册，请更新手机号");
            }
            sysUserEntity.setNumber("u"+sysUserEntity.getPhone());
            boolean add = sysUserService.add(sysUserEntity);
            if(add){
                return R.ok("添加成功","");
            }
        }
        return R.error(500,"添加失败");
    }

    @ApiOperation(httpMethod = "GET", value = "删除用户", response = R.class,
            notes = "删除用户")
    @GetMapping("/del")
    @UserLoginToken
    @Override
    public R del(Long id) {
        if(id>0){
            boolean del = sysUserService.del(id);
            if(del){
                return R.ok("删除成功","");
            }
        }
        return R.error(500,"删除失败");
    }

    @ApiOperation(httpMethod = "POST", value = "更新用户信息", response = R.class,
            notes = "更新用户信息")
    @PostMapping("/update")
    @UserLoginToken
    @Override
    public R update(@RequestBody SysUserEntity sysUserEntity) {
        if(sysUserEntity!=null&&sysUserEntity.getId()>0){
            boolean update = sysUserService.update(sysUserEntity);
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

    @ApiOperation(httpMethod = "GET", value = "获取当前用户信息", response = R.class,
            notes = "系统用户详情")
    @Override
    @GetMapping("/info")
    @UserLoginToken
    public R get(SysUserEntity sysUserEntity) {
        String tokenUserNumber = TokenUtil.getTokenUserNumber();
        SysUserEntity sysUser = sysUserService.findUserByNumber(tokenUserNumber);
        LogUtil.i("当前用户信息：",sysUser);
        return R.ok("获取用户详情",sysUser);
    }

	public static void main(String[] args) {
		System.out.println(DigestUtils.md5DigestAsHex("123456".getBytes()));
	}

    @ApiOperation(httpMethod = "POST", value = "登录", response = R.class,
            notes = "系统用户登录")
    @PostMapping("/login")
    public R login(@RequestBody SysUserEntity user) {
        if(user!=null&&!StringUtil.isEmpty(user.getNumber())&&!StringUtil.isEmpty(user.getPassword())){
            //查询数据库
            SysUserEntity dbUser = sysUserService.findUserByNumber(user.getNumber());
            if(dbUser!=null){
                String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
                if(md5Password.equals(dbUser.getPassword())){
                    //获取token
                    String token = tokenService.getToken(dbUser);
                    dbUser.setToken(token);
                    LogUtil.i("当前登录用户信息：",dbUser);
                    return R.ok("登录成功",dbUser);
                }
            }
            return R.error(500,"账户名或者密码错误");
        }
        return R.error(500,"账户名或者密码不可为null");
    }

    @ApiOperation(httpMethod = "POST", value = "退出登录", response = R.class,
            notes = "系统用户退出登录")
    @PostMapping("/logout")
    @UserLoginToken
    public R logout(){
        LogUtil.i("退出登录：","");
        return R.ok("退出登录","");
    }

    @ApiOperation(httpMethod = "POST", value = "修改密码", response = R.class,
            notes = "系统用户修改密码")
    @PostMapping("/updatePassword")
    @UserLoginToken
    public R updatePassword(@RequestBody Map<String,String> map){
        LogUtil.i("用户更改密码：",map);
        if(map!=null){
            String number = TokenUtil.getTokenUserNumber();
            if(!map.containsKey("number")){
                return R.error(500,"更改密码失败，当前用户不是修改用户");
            }else if(!number.equals(map.get("number"))){
                return R.error(500,"更改密码失败，当前用户不是修改用户");
            }
            if(!map.containsKey("oldpwd")){
                return R.error(500,"旧密码不可为null");
            }
            if(!map.containsKey("newpwd")){
                return R.error(500,"新密码不可为null");
            }
            //查询用户
            SysUserEntity user = sysUserService.findUserByNumber(number);
            if(user!=null){
                String oldpwd = DigestUtils.md5DigestAsHex(map.get("oldpwd").getBytes());
                String newpwd = DigestUtils.md5DigestAsHex(map.get("newpwd").getBytes());
                if(oldpwd.equals(user.getPassword())){
                    //更新
                    user.setPassword(newpwd);
                    boolean update = sysUserService.update(user);
                    if(update){
                        return R.ok("修改成功","");
                    }else{
                        return R.error(500,"更改密码失败");
                    }
                }else{
                    return R.error(500,"旧密码错误");
                }
            }
        }
        //先判断是否是当前用户
        return R.error(500,"更改密码失败");
    }

    @ApiOperation(httpMethod = "POST", value = "修改信息", response = R.class,
            notes = "系统用户修改信息")
    @PostMapping("/updateInfo")
    @UserLoginToken
    public R updateUserInfo(@RequestBody SysUserEntity sysUserEntity){
        LogUtil.i("用户数据修改：",sysUserEntity);
        if(sysUserEntity!=null){
            String number = TokenUtil.getTokenUserNumber();
            if(!sysUserEntity.getNumber().equals(number)){
                return R.error(500,"更改用户信息失败，当前用户不是修改用户");
            }
            //先查询，在更新
            SysUserEntity user = sysUserService.findUserById(sysUserEntity.getId());
            SpringBeanUtil.copyPropertiesIgnoreNull(sysUserEntity,user);
            boolean update = sysUserService.update(user);
            if(update){
                return R.ok("修改成功","");
            }else{
                return R.error(500,"修改用户信息失败");
            }
        }
        //先判断是否是当前用户
        return R.error(500,"更改用户信息失败");
    }

    /***
     * 这个请求需要验证token才能访问
     *
     * @author: MRC
     * @date 2019年5月27日 下午5:45:19
     * @return String 返回类型
     */
    @UserLoginToken
    @GetMapping("/getMessage")
    public String getMessage() {
        // 取出token中带的用户number 进行操作
        System.out.println(TokenUtil.getTokenUserNumber());
        return "你已通过验证";
    }

    @ApiOperation(httpMethod = "POST", value = "分页获取用户信息", response = R.class,
            notes = "分页获取用户信息")
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
            PageEntity<SysUserEntity> pages = sysUserService.getPage(page, size);
            return R.ok("获取用户列表",pages);
        }
        return R.ok("获取用户列表","");
    }
}
