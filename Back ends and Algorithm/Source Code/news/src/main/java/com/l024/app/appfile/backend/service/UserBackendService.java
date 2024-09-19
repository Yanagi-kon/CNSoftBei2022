package com.l024.app.appfile.backend.service;

import com.l024.app.appfile.entity.History;

import java.util.Map;

public interface UserBackendService {
    Map<String, Object> getUserList(int page, int size);

    Map<String, Object> getHistory(int page, int size, Integer uid);

    History getHistoryById(History history);
}
