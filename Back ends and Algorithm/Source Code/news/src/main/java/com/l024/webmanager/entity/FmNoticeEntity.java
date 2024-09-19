package com.l024.webmanager.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "fm_notice")
@ApiModel(value="公告留言信息")
public class FmNoticeEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String message;
    private Timestamp createTime;
    private Byte type;

    @ManyToOne(targetEntity = SysUserEntity.class)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private SysUserEntity user;
}
