package com.l024.webmanager.service;

import com.l024.webmanager.entity.FmDepartmentEntity;
import com.l024.webmanager.entity.PageEntity;
import com.l024.webmanager.repository.FmDepartmentRepository;
import com.l024.webmanager.util.LogUtil;
import com.l024.webmanager.util.StringUtil;
import com.l024.webmanager.vo.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 部门管理
 */
@Service
public class FmDepartmentService implements BaseService<FmDepartmentEntity> {

    @Autowired
    private FmDepartmentRepository fmDepartmentRepository;

    @Override
    public boolean add(FmDepartmentEntity fmDepartmentEntity) {
        fmDepartmentEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        FmDepartmentEntity save = fmDepartmentRepository.save(fmDepartmentEntity);
        if(save!=null){
            return true;
        }
        return false;
    }

    @Override
    public boolean del(long id) {
        fmDepartmentRepository.deleteById(id);
        return true;
    }

    @Override
    public List<FmDepartmentEntity> getAll() {
        return null;
    }

    @Override
    public boolean update(FmDepartmentEntity fmDepartmentEntity) {
        //先查询
        FmDepartmentEntity departmentEntity = fmDepartmentRepository.findById(fmDepartmentEntity.getId()).get();
        if(departmentEntity!=null){
            SpringBeanUtil.copyPropertiesIgnoreNull(fmDepartmentEntity,departmentEntity);
            fmDepartmentRepository.save(departmentEntity);
            LogUtil.i("更新部门信息：",departmentEntity);
            return fmDepartmentRepository.save(departmentEntity) != null;
        }
        return false;
    }

    public boolean getDepartmentByName(String name){
        if(!StringUtil.isEmpty(name)){
            FmDepartmentEntity departmentEntity = fmDepartmentRepository.findFmDepartmentEntityByName(name);
            LogUtil.i("查询到的部门:",departmentEntity);
            if(departmentEntity!=null){

                return true;
            }
        }
        return false;
    }

    /**
     * 分页获取部门数据
     * @param page
     * @param size
     * @return
     */
    public PageEntity<FmDepartmentEntity> getPages(int page,int size){
        if(page<0){
            page = 0;
        }
        if(size<0){
            size = 10;
        }
        PageRequest pageRequest =PageRequest.of(page,size);
        Page<FmDepartmentEntity> fmCompanyEntityPage = fmDepartmentRepository.findAll(pageRequest);
        if(fmCompanyEntityPage!=null){
            PageEntity<FmDepartmentEntity> pages = new PageEntity<>();
            pages.setCurrentPage(fmCompanyEntityPage.getNumber());
            pages.setTotalElements(fmCompanyEntityPage.getTotalElements());
            pages.setTotalPage(fmCompanyEntityPage.getTotalPages());
            pages.setContent(fmCompanyEntityPage.getContent());
           LogUtil.i("总记录数：",fmCompanyEntityPage.getTotalElements());
           LogUtil.i("当前第几页：",fmCompanyEntityPage.getNumber());
           LogUtil.i("总页数：",fmCompanyEntityPage.getTotalPages());
           LogUtil.i("当前页面的List",fmCompanyEntityPage.getContent());
           LogUtil.i("当前记录数：",fmCompanyEntityPage.getNumberOfElements());
           return pages;
        }
        return null;
    }

    public List<Map<String,Object>> getNames(){
        return fmDepartmentRepository.getDepartmentNames();
    }
}
