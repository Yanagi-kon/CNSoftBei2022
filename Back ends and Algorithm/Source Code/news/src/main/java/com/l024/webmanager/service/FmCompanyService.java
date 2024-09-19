package com.l024.webmanager.service;

import com.l024.webmanager.entity.FmCompanyEntity;
import com.l024.webmanager.repository.FmCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公司详情
 */
@Service
public class FmCompanyService implements BaseService<FmCompanyEntity> {
    @Autowired
    private FmCompanyRepository fmCompanyRepository;
    @Override
    public boolean add(FmCompanyEntity fmCompanyEntity) {
        return false;
    }

    @Override
    public boolean del(long id) {
        return false;
    }

    @Override
    public List<FmCompanyEntity> getAll() {
        return fmCompanyRepository.findAll();
    }

    @Override
    public boolean update(FmCompanyEntity fmCompanyEntity) {
        return false;
    }
}
