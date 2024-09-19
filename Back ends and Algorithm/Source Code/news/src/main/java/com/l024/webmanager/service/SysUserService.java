package com.l024.webmanager.service;

import com.l024.webmanager.entity.PageEntity;
import com.l024.webmanager.entity.SysUserEntity;
import com.l024.webmanager.repository.SysUserRepository;
import com.l024.webmanager.util.StringUtil;
import com.l024.webmanager.vo.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * 系统用户相关操作
 */
@Service
public class SysUserService implements BaseService<SysUserEntity>{
    @Autowired
    private SysUserRepository sysUserRepository;


    @Override
    public boolean add(SysUserEntity sysUserModel) {
        if(sysUserModel!=null){
            String md5Password = DigestUtils.md5DigestAsHex(sysUserModel.getPassword().getBytes());
            sysUserModel.setPassword(md5Password);
            sysUserModel.setCreateTime(new Timestamp(System.currentTimeMillis()));
            SysUserEntity save = sysUserRepository.save(sysUserModel);
            if(save!=null){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean del(long id) {
        if(id>0){
            sysUserRepository.deleteById(id);
            return  true;
        }
        return false;
    }

    @Override
    public List<SysUserEntity> getAll() {
        return null;
    }

    @Override
    public boolean update(SysUserEntity sysUserModel) {
        if(sysUserModel!=null&&sysUserModel.getId()>0){
            //查询数据库
            SysUserEntity dbUser = sysUserRepository.findById(sysUserModel.getId()).get();
            SpringBeanUtil.copyPropertiesIgnoreNull(sysUserModel,dbUser);
            SysUserEntity save = sysUserRepository.save(dbUser);
            if(save!=null){
                return true;
            }
        }
        return false;
    }

    //根据id查询
    public SysUserEntity findUserById(long userId) {
        if(userId>0){
            Optional<SysUserEntity> optional = sysUserRepository.findById(userId);
            if(optional.isPresent()){
                return optional.get();
            }
        }
        return null;
    }

    /**
     * 根据编号查询
     * @param number
     * @return
     */
    public SysUserEntity findUserByNumber(String number){
        if(!StringUtil.isEmpty(number)){
           return sysUserRepository.querySysUserEntitiesByNumber(number);
        }
        return null;
    }

    /**
     * 根据编号查询
     * @param phone
     * @return
     */
    public SysUserEntity findUserByPhone(String phone){
        if(!StringUtil.isEmpty(phone)){
            return sysUserRepository.querySysUserEntitiesByPhone(phone);
        }
        return null;
    }


    /**
     * 分页获取
     * @param page
     * @param size
     * @return
     */
    public PageEntity<SysUserEntity> getPage(int page, int size){
        if(page<0){
            page = 0;
        }
        if(size<0){
            size = 10;
        }
        PageRequest pageRequest =PageRequest.of(page,size);
        Page<SysUserEntity> fmCompanyEntityPage = sysUserRepository.findAll(pageRequest);
        if(fmCompanyEntityPage!=null){
            PageEntity<SysUserEntity> pages = new PageEntity<>();
            pages.setCurrentPage(fmCompanyEntityPage.getNumber());
            pages.setTotalElements(fmCompanyEntityPage.getTotalElements());
            pages.setTotalPage(fmCompanyEntityPage.getTotalPages());
            pages.setContent(fmCompanyEntityPage.getContent());
            return pages;
        }
        return null;
    }
}
