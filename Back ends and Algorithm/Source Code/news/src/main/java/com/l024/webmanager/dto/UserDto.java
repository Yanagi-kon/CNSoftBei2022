package com.l024.webmanager.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

/**
 * @Auther: Mr.yang
 * @Date: 2022/06/04/23:29
 * @Description: 描述：
 */

@Data
public class UserDto  {
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public  enum Vision{
        ONE_VISION("一级低视力"),
        TWO_VISION("二级低视力"),
        ONE_BLIND("一级盲"),
        TWO_BLIND("二级盲");

        private final String value;
    }
    @ApiModelProperty("视障等级 一级低视力，二级低视力 一级盲，二级盲")
    private String vision;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "手机号")
    private String tel;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty("简介")
    private String introduction;

    @ApiModelProperty("生日")
    private String birthday;

    @ApiModelProperty("uid")
    private String id;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("地区")
    private String area;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区")
    private String district;


}
