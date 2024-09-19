package com.l024.app.appfile.service.Impl;

import com.l024.app.appfile.entity.History;
import com.l024.app.appfile.repo.HistoryRepository;
import com.l024.app.appfile.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther:
 * @Date: 2022/06/05/5:29
 * @Description: 描述：
 */
@Service
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    private HistoryRepository historyRepository;
    @Override
    public void saveHistory(History history) {
       historyRepository.save(history);
    }
}
