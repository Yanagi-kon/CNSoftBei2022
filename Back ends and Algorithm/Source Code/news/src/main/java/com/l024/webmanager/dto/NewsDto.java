package com.l024.webmanager.dto;

import com.l024.app.appfile.entity.News;
import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @Auther: Mr.yang
 * @Date: 2022/06/07/21:03
 * @Description: 描述：
 */
@Data
public class NewsDto extends News {
    private Integer count;

    private  Integer sum;
}
