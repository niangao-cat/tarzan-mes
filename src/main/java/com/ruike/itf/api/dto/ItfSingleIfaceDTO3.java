package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/9 16:04
 */
@Data
public class ItfSingleIfaceDTO3 implements Serializable {

    @ApiModelProperty(value = "工位")
    private String workcellCode;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;

    @ApiModelProperty(value = "SN号")
    private String materialLotCode;

    @ApiModelProperty(value = "员工账号")
    private String user;

    @ApiModelProperty(value = "出站类型")
    private String outSiteAction;

    @ApiModelProperty(value = "交叉复测标识")
    private String crossRetestFlag;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @ApiModelProperty(value = "默认站点ID")
    private String defaultSiteId;

    @ApiModelProperty(value = "容器Id")
    private String containerId;
}
