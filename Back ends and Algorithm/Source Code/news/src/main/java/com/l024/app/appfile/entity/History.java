package com.l024.app.appfile.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Auther: Mr.yang
 * @Date: 2022/06/05/5:20
 * @Description: 描述：
 */
@Data
@Entity
@Table(name="history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "type")
    String type;

    @Column(name = "create_time")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8"
            )
    Date createTime;

    @Column(name = "uid")
    Integer uid;

    @Column(name = "type_name")
    String typeName;
    @Column(name = "detail")
    String detail;
}
