package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WmsStandingWarehouseEntryReviewDTO implements Serializable {
    private static final long serialVersionUID = -795747650970272710L;

    @ApiModelProperty(value = "tenantId")
    private  Long tenantId;
    @ApiModelProperty(value = "容器编码")
    private String containerCode;
    @ApiModelProperty(value = "容器货位编码")
    private String  locatorCode;
    @ApiModelProperty(value = "容器货位Id")
    private Long locatorId;
    @ApiModelProperty(value = "容器货位名称")
    private String locatorName;
    @ApiModelProperty(value = "条码集合")
    private List<WmsStandingWarehouseEntryReviewDTO2> list;
    @ApiModelProperty(value = "复核确认")
    private String checkConfirm;
    @ApiModelProperty(value = "复核取消")
    private String checkCancel;
}
