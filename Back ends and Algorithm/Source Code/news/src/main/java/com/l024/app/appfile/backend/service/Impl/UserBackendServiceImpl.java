package com.l024.app.appfile.backend.service.Impl;

import com.l024.app.appfile.backend.service.UserBackendService;
import com.l024.app.appfile.entity.History;
import com.l024.app.appfile.entity.User;
import com.l024.app.appfile.repo.HistoryRepository;
import com.l024.app.appfile.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserBackendServiceImpl implements UserBackendService {
    private final UserRepository userRepository;

    public UserBackendServiceImpl(UserRepository userRepository, HistoryRepository repository) {
        this.userRepository = userRepository;
        this.repository = repository;
    }
    private final HistoryRepository repository;

    @Override
    public Map<String, Object> getUserList(int page, int size) {
         if (page<1){
             page=1;
         }
         if (size<1){
             size=10;
         }
        List<User> all = userRepository.findAll();
         List<User> newsPage = new ArrayList<>();
        for(int i=0;i<size;i++){
            if(((page - 1) * size + i)<all.size()){
                newsPage.add(all.get((page - 1) * size + i));
            }
        }
        Map<String,Object> map  =  new HashMap<>();
        map.put("news",newsPage);
        Map<String,Object> pages = new HashMap<>();
        pages.put("total",all.size());
        pages.put("content",newsPage.size());
        pages.put("page",page);
        pages.put("size",size);
        map.put("page",pages);
        return map;
    }

    @Override
    public Map<String, Object> getHistory(int page, int size, Integer uid) {
        List<History> all = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();

        if (uid==null||uid.equals("")){
            all  = repository.getAll();
        }else {
            all =   repository.getByUid(uid);
        }
        List<History> newsPage = new ArrayList<>();
//        for(int i=0;i<size;i++){
//            if(((page - 1) * size + i)<all.size()){
//                newsPage.add(all.get((page - 1) * size + i));
//            }
//        }
        map.put("news",all);
        Map<String,Object> pages = new HashMap<>();
        pages.put("total",all.size());
        pages.put("content",newsPage.size());
        pages.put("page",page);
        pages.put("size",size);
        map.put("page",pages);
        return map;
    }

    @Override
    public History getHistoryById(History history) {
      History byId =    repository.getHistoryById(String.valueOf(history.getId()));
        return byId;
    }
}
