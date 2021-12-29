package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeSnLabCodeDTO
 * SN实验代码导入DTO
 * @author: chaonan.hu@hand-china.com 2021/04/01 09:33:21
 **/
@Data
public class HmeSnLabCodeDTO implements Serializable {
    private static final long serialVersionUID = -2611282794813181742L;

    @ApiModelProperty(value = "站点", required = true)
    private String siteCode;

    @ApiModelProperty(value = "条码编号", required = true)
    private String materialLotCode;

    @ApiModelProperty(value = "工艺编号", required = true)
    private String operationName;

    @ApiModelProperty(value = "工艺版本", required = true)
    private String revision;

    @ApiModelProperty(value = "实验代码", required = true)
    private String labCode;

    @ApiModelProperty(value = "实验备注")
    private String remark;

    @ApiModelProperty(value = "有效性", required = true)
    private String enableFlag;
}
