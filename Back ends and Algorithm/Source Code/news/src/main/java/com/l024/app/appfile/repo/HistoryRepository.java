package com.l024.app.appfile.repo;

import com.l024.app.appfile.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: Mr.yang
 * @Date: 2022/06/05/5:39
 * @Description: 描述：
 */
@Repository
public interface HistoryRepository extends JpaRepository<History,Integer> {
    @Query("select s from  History as s where s.uid=?1 order by s.createTime DESC")
    List<History> getByUid(Integer uid);
    @Query("select s from  History as s order by s.createTime DESC ")
    List<History> getAll();
    @Query("SELECT s FROM  History AS s where s.id=?1")
    History getHistoryById(String id);

    List<History> findHistoryByUidAndType(Integer uid, String type);
}
