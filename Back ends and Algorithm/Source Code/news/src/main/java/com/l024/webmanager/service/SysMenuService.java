package com.l024.webmanager.service;

import com.l024.webmanager.entity.SysMenuEntity;
import com.l024.webmanager.repository.SysMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单
 */
@Service
public class SysMenuService{
    @Autowired
    private SysMenuRepository sysMenuRepository;

    public List<SysMenuEntity> getAll() {
        List<SysMenuEntity> all = sysMenuRepository.findAll();
        if(all!=null&&all.size()>0){
            List<SysMenuEntity> treeMenu = getTreeMenu(all);
            return treeMenu;
        }
        return null;
    }


    /**
     * 获取树形菜单
     * @param sysMenus
     * @return
     */
    public List<SysMenuEntity> getTreeMenu(List<SysMenuEntity> sysMenus){
        if(sysMenus!=null&&sysMenus.size()>0){
            //获取父元素
            List<SysMenuEntity> treeMenus = sysMenus.stream().filter(sysMenu -> {
                return sysMenu.getParentId()==0;
            }).map((menu)->{
                menu.setChildren(getChildrens(menu,sysMenus));
                return menu;
            }).sorted((menu1,menu2)->{
                return (menu1.getOrderNum()==0?0:menu1.getOrderNum())-(menu2.getOrderNum()==0?0:menu2.getOrderNum());
            }).collect(Collectors.toList());
            return treeMenus;
        }
        return null;
    }

    /**
     * 递归查找所有菜单的子菜单
     * @param root
     * @param all
     * @return
     */
    private List<SysMenuEntity> getChildrens(SysMenuEntity root, List<SysMenuEntity> all){
        List<SysMenuEntity> children = all.stream().filter((categoryEntity)->{
            return categoryEntity.getParentId() == root.getId();
        }).map((categoryEntity)->{
            //1.递归查找子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity,all));
            return categoryEntity;
        }).sorted((menu1,menu2)->{
            //菜单排序
            return menu1.getOrderNum()-menu2.getOrderNum();
        }).collect(Collectors.toList());
        return children;
    }
}
