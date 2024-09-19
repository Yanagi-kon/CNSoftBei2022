package com.l024.app.appfile.repo;

import com.l024.app.appfile.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findById(int id);

//    @Override
//    <S extends User> List<S> findAll(Example<S> example);

    @Transactional
    @Modifying
    int deleteAllById(int uid);
    @Transactional    //DML操作需要事务环境，可以不在这里声明，但是调用时一定要处于事务环境下
    @Modifying     //表示这是一个DML操作
    @Query("update User set nickname = ?2 ,tel=?3 ,sex=?4,area=?5,birthday=?6,email=?7,introduction=?8 where id = ?1")
    int updateInfo(int uid, String nickaname,String tel,String sex,String area,String birthday,String email,String introduction);

    User findUserByFacecode(String facecode);

    @Transactional
    @Modifying
    @Query("update User set nickname = ?2 ,tel=?3 ,sex=?4,area=?5,birthday=?6,email=?7,introduction=?8 ,vision = ?9 where id = ?1")
    void updateInfo1(int parseInt, String nickname, String tel, String sex, String area, String birthday, String email, String introduction, String vision);
}
