package com.l024.app.appfile.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="user")
public class User {
    public User(String facecode) {
        this.facecode = facecode;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    int id;
    @Column(name = "nickname")
    String nickname;

    @Column(name = "tel")
    String tel;

    @Column(name = "sex")
    String sex;

    @Column(name = "area")
    String area;

    @Column(name = "province")
    String province;

    @Column(name = "city")
    String city;

    @Column(name = "district")
    String district;

    @Column(name = "birthday")
    String birthday;

    @Column(name = "email")
    String email;

    @Column(name = "introduction")
    String introduction;

    @Column(name = "facecode")
    String facecode;

    @Column(name = "vision")
    @ApiModelProperty("视障等级")
    String vision;
    public User() {

    }
}
