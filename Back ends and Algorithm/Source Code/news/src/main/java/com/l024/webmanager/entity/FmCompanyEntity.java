package com.l024.webmanager.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "team")
@ApiModel(value="团队信息")
public class FmCompanyEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private Timestamp createTime;
    private String count;
    private Double money;
    private String icon;
}
