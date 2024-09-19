package com.l024.app.appfile.controller;

import com.l024.app.appfile.entity.History;
import com.l024.app.appfile.entity.News;
import com.l024.app.appfile.entity.User;
import com.l024.app.appfile.entity.resp.RestBean;
import com.l024.app.appfile.repo.HistoryRepository;
import com.l024.app.appfile.repo.NewsRepository;
import com.l024.app.appfile.repo.UserRepository;
import com.l024.app.appfile.service.UserService;
import com.l024.app.appfile.utils.GetKeywords;
import com.l024.webmanager.dto.UserDto;
import com.l024.webmanager.exception.RRException;
import com.l024.webmanager.vo.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserApiController {
    @Resource
    UserRepository repository;
    @Resource
    UserService userService;

    @PostMapping("/getUser")
    public RestBean<User> getUser(int uid){
        User user=userService.getUser(uid);
        if(user!=null) {
            return new RestBean<>(200, "查找成功！", user);
        }else{
            return new RestBean<>(403,"UID不存在！",null);
        }
    }

    @PostMapping("/getUserAll")
    public RestBean<List<User>> getAllUser(){
        List<User> userList= userService.getAllUsers();
        return new RestBean<>(200, "查找成功！", userList);
    }

    @PostMapping("/getUserPage")
    public RestBean<List<User>> getUserPage(int page,int size){
        List<User> userPage= userService.getUserPage(page,size);
        return new RestBean<>(200, "查找成功！", userPage);
    }

    @RequestMapping("/updateInfo")
    public RestBean<String> updateinfo(int uid, String nickaname, String tel, String sex, String area, String birthday, String email, String introduction){
        userService.UpdateUserInfo(uid,nickaname, tel, sex, area, birthday, email, introduction);
        return new RestBean<>(200,"请求成功！","success");
    }

    @RequestMapping("/delUser")
    public RestBean<String> delUser(@RequestBody Map<String,Integer> uid){
        userService.DelUser(uid.get("uid"));
        return new RestBean<String>(200,"删除成功！","success");
    }

    //注册，输入人脸base64照片，分配uid，并将照片base64值存入sql,在人脸库创建人脸，返回uid
    @RequestMapping("/userRegister")
    public RestBean<Integer> userRegister(String image){
        System.out.println("这里是注册");
        int uid = userService.UserRegister(image);
        System.out.println("注册uid:"+uid);
        return new RestBean<>(200,"注册成功！",uid);
    }

    //登录，输入人脸base64照片与人脸库已有人脸匹配，匹配到相似度大于0.5就返回uid
    //返回字符串：uid值，"no face"，"face search failed","no live"
    @RequestMapping("/userLogin")
    public RestBean<String> userLogin(String image){
        String result= userService.UserLogin(image);
        return new RestBean<>(200,"请求成功！",result);
        //结果要看返回的result，“请求成功”是统一的
        //登录成功返回的是uid（String类型）
    }
    @PostMapping("/personalCenter")
    @ApiOperation("个人中心完善")
    public RestBean<String> personalCenter(UserDto user){

        userService.personalCenter(user);
        return  new RestBean<String>(200,"ok");
    }

    @Resource
    HistoryRepository historyRepository;
    @Resource
    NewsRepository newsRepository;
    @RequestMapping("/wordcloud")
    public RestBean<List<List>> getWordCloud(int uid){
        List<History> historyList=new ArrayList<>();
        historyList=historyRepository.findHistoryByUidAndType(uid,"1");
        String newsText="";
        for (int i=0;i<historyList.size();i++) {
            History history = historyList.get(i);
            String title = history.getDetail();
            News news = newsRepository.findNewsByTitle(title);
            if(news!=null){
                newsText=newsText+news.getText();
            }

        }
        List<List> keylist= GetKeywords.getKeywords(newsText);
        return new RestBean<>(200,"请求成功！",keylist);
    }




}
