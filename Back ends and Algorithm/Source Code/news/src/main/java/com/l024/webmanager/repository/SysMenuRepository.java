package com.l024.webmanager.repository;

import com.l024.webmanager.entity.SysMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 菜单
 */
public interface SysMenuRepository extends JpaRepository<SysMenuEntity,Long> {
}
