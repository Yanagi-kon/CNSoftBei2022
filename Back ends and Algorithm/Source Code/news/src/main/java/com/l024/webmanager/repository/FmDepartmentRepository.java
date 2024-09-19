package com.l024.webmanager.repository;

import com.l024.webmanager.entity.FmDepartmentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 */
public interface FmDepartmentRepository extends PagingAndSortingRepository<FmDepartmentEntity,Long> {
    FmDepartmentEntity findFmDepartmentEntityByName(String name);

    @Query(value = "select id,name,money from fm_department",nativeQuery=true)
    public List<Map<String,Object>> getDepartmentNames();
}
