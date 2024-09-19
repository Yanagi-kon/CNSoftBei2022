package com.l024.webmanager.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name ="sys_menu")
@ApiModel(value="系统菜单")
public class SysMenuEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;
    private Long parentId;
    private String name;
    private String url;
    private Integer type;
    private String icon;
    private Integer orderNum;
    @Transient
    private List<SysMenuEntity> children;

}
