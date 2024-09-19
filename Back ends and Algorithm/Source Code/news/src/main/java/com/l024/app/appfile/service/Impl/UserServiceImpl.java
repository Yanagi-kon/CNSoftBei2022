package com.l024.app.appfile.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.l024.app.appfile.dao.NewsMapper;
import com.l024.app.appfile.entity.User;
import com.l024.app.appfile.repo.UserRepository;
import com.l024.app.appfile.service.UserService;
import com.l024.webmanager.dto.UserDto;
import com.l024.webmanager.exception.RRException;
import com.l024.webmanager.util.ObjUtils;
import com.l024.webmanager.util.StringUtil;
import face.Face;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserRepository repository;

    @Autowired
    private NewsMapper newsMapper;

    @Override
    public User getUser(int uid) {
        User user=repository.findById(uid);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList=repository.findAll();
        System.out.println("userlist:"+userList);
        return userList;
    }

    @Override
    public List<User> getUserPage(int page, int size) {
        if(page<1){
            page = 1;
        }
        if(size<0){
            size = 10;
        }
        List<User> userList = repository.findAll();
        List<User> userPage=new ArrayList<User>();
        for(int i=0;i<size;i++){
            if(((page - 1) * size + i)<userList.size()){
                userPage.add(userList.get((page - 1) * size + i));
            }

        }

        return userPage;
    }

    @Override
    public void UpdateUserInfo(int uid,String nickaname, String tel, String sex, String area, String birthday, String email, String introduction) {
        //User user=repository.findAllById(uid);
        repository.updateInfo(uid,nickaname, tel, sex, area, birthday, email, introduction);

    }

    @Override
    public void DelUser(int uid) {
        repository.deleteAllById(uid);
    }

    @Override
    public int UserRegister(String image) {
        //注册，检测照片中是否有人脸,有就分配uid并存入数据库，返回uid

        String replace = image.replace("data:image/jpeg;base64,", "");
        if(Face.FaceDetect(replace)!=null){
            //返回1是活体
            if(Face.LivenessDetect(replace)==1){
                User user=new User(replace);
                User user2=repository.save(user);
                int uid=user2.getId();
                Face.CreateFace("user"+uid,replace); //人脸库创建人员，名字为"user"+uid
                return uid;
            }

        }
        return 0;
    }

    @Override
    public String UserLogin(String image) {
        String replace = image.replace("data:image/jpeg;base64,", "");
        //登录，image参数为登录时摄像头拍的人脸
        if (Face.FaceDetect(replace) == null) {
            return "no face";
        } else {
            if (Face.LivenessDetect(replace) == 1){
                List<String> userInfo=Face.FaceSearch(replace);
                //userInfo第0个元素是匹配到的人脸库中的名字，第1个元素是相似度
                if(Double.parseDouble(userInfo.get(0))>=0.5){
                    int uid=Integer.parseInt((userInfo.get(1)).substring(4));
                    return String.valueOf(uid);  //登录成功
                }
                else
                    return "face search failed";
            } else {
                    //非活体
                    return "no live";
                }
            }
    }


    /**
     * 个人中心完善资料
     * @param user
     */
    @Override
    public void personalCenter(UserDto user) {
        if (user.getId()==null||user.getId().equals("")){
            throw   new RRException("找不到个登录信息");
        }
      if (ObjUtils.isNotNull(user)){
         throw  new RRException("至少填写一项信息");
      }

        newsMapper.updateUserInfo(user);


    }

}
