package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 反射镜数据接口接收返回DTO
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/4 20:01
 */
@Data
public class InSIteDTO {
    @ApiModelProperty(value = "工位")
    private String workcellCode;

    @ApiModelProperty(value = "条码")
    private String materialLotCode;

    @ApiModelProperty(value = "jobType")
    private String jobType;

    @ApiModelProperty(value = "用户")
    private String user;
}
