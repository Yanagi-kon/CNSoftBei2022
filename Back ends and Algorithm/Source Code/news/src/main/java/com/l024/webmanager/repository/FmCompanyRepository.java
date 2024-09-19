package com.l024.webmanager.repository;

import com.l024.webmanager.entity.FmCompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 公司详情
 */
public interface FmCompanyRepository extends JpaRepository<FmCompanyEntity,Long> {
}
