package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/15 14:06
 */
@Data
public class ItfFirstProcessIfaceDTO2 implements Serializable {

    private static final long serialVersionUID = 8265334468951437884L;

    @ApiModelProperty(value = "工位")
    private String workcellCode;

    @ApiModelProperty(value = "SN号")
    private String snNum;

    @ApiModelProperty(value = "员工账号")
    private String user;

    @ApiModelProperty(value = "扫描条码（集合）")
    private String materialLotCode;

    @ApiModelProperty(value = "芯片位置")
    private String chipLocation;

    @ApiModelProperty(value = "工单")
    private String workOrderNum;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @ApiModelProperty(value = "默认站点ID")
    private String defaultSiteId;
}
