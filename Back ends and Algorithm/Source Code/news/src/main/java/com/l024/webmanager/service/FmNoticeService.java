package com.l024.webmanager.service;

import com.l024.webmanager.entity.FmNoticeEntity;
import com.l024.webmanager.entity.PageEntity;
import com.l024.webmanager.entity.SysUserEntity;
import com.l024.webmanager.repository.FmNoticeRepository;
import com.l024.webmanager.util.StringUtil;
import com.l024.webmanager.util.TokenUtil;
import com.l024.webmanager.vo.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * 公司公告留言
 */
@Service
public class FmNoticeService implements BaseService<FmNoticeEntity> {
    @Autowired
    private FmNoticeRepository fmNoticeRepository;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public boolean add(FmNoticeEntity fmNoticeEntity) {
        String tokenUserNumber = TokenUtil.getTokenUserNumber();
        if(!StringUtil.isEmpty(tokenUserNumber)&&fmNoticeEntity!=null){
            //查询客户
            SysUserEntity user = sysUserService.findUserByNumber(tokenUserNumber);
            if(user!=null){
                fmNoticeEntity.setUser(user);
                fmNoticeEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));

                FmNoticeEntity save = fmNoticeRepository.save(fmNoticeEntity);
                if(save!=null){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean del(long id) {
        if(id>0){
            fmNoticeRepository.deleteById(id);
            return  true;
        }
        return false;
    }

    @Override
    public List<FmNoticeEntity> getAll() {
        return null;
    }

    @Override
    public boolean update(FmNoticeEntity fmNoticeEntity) {
        if(fmNoticeEntity!=null&&fmNoticeEntity.getId()>0){
            //根据id查询
            FmNoticeEntity dbNoticeEntity = fmNoticeRepository.findById(fmNoticeEntity.getId()).get();
            SpringBeanUtil.copyPropertiesIgnoreNull(fmNoticeEntity,dbNoticeEntity);
            FmNoticeEntity save = fmNoticeRepository.save(dbNoticeEntity);
            if(save!=null){
                return true;
            }
        }
        return false;
    }

    /**
     * 分页获取
     * @param page
     * @param size
     * @return
     */
    public PageEntity<FmNoticeEntity> getPage(int page, int size){
        if(page<0){
            page = 0;
        }
        if(size<0){
            size = 10;
        }
        PageRequest pageRequest =PageRequest.of(page,size);
        Page<FmNoticeEntity> fmCompanyEntityPage = fmNoticeRepository.findAll(pageRequest);
        if(fmCompanyEntityPage!=null){
            PageEntity<FmNoticeEntity> pages = new PageEntity<>();
            pages.setCurrentPage(fmCompanyEntityPage.getNumber());
            pages.setTotalElements(fmCompanyEntityPage.getTotalElements());
            pages.setTotalPage(fmCompanyEntityPage.getTotalPages());
            pages.setContent(fmCompanyEntityPage.getContent());
            return pages;
        }
        return null;
    }
}
