package com.l024.app.appfile.service;

import com.l024.app.appfile.entity.User;
import com.l024.webmanager.dto.UserDto;

import java.util.List;

public interface UserService {
    User getUser(int uid);
    List<User> getAllUsers();

    List<User> getUserPage(int page, int size);

    void UpdateUserInfo(int uid, String nickaname, String tel, String sex, String area, String birthday, String email, String introduction);

    void DelUser(int uid);
    int UserRegister(String image);

    String UserLogin(String image);

    void personalCenter(UserDto user);
}
