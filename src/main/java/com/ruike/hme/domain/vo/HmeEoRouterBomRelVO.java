package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.Date;

/**
 * @Classname HmeEoRouterBomRelVO
 * @Description HmeEoRouterBomRelVO
 * @Date 2020/12/31 13:50
 * @Author yuchao.wang
 */
@Data
public class HmeEoRouterBomRelVO implements Serializable {
    private static final long serialVersionUID = -8799274642507568313L;

    @ApiModelProperty("主键")
    private String eoRouterBomRelId;

    @ApiModelProperty(value = "EO ID")
    private String eoId;

    @ApiModelProperty(value = "工艺路线ID")
    private String routerId;

    @ApiModelProperty(value = "装配清单ID")
    private String bomId;

    @ApiModelProperty(value = "装配清单名称")
    private String bomName;

    @ApiModelProperty(value = "创建时间")
    private Date creationDate;
}