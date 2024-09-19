package com.l024.webmanager.repository;

import com.l024.webmanager.entity.FmNoticeEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 公司留言
 */
public interface FmNoticeRepository extends PagingAndSortingRepository<FmNoticeEntity,Long> {
}
