package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * 备料需求单据
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/24 20:20
 */
@Data
public class WmsPrepareExecInsDocVO {
    @ApiModelProperty("租户")
    private Long tenantId;
    @ApiModelProperty("配送单ID")
    private String instructionDocId;
    @ApiModelProperty("配送单号")
    private String instructionDocNum;
    @ApiModelProperty("产线ID")
    private String prodLineId;
    @ApiModelProperty("产线编码")
    private String prodLineCode;
    @ApiModelProperty("工段")
    private String workcellCode;
    @ApiModelProperty("目标货位ID")
    private String toLocatorId;
    @ApiModelProperty("目标货位编码")
    private String toLocatorCode;
    @ApiModelProperty("单据状态")
    @LovValue(lovCode = "WMS.DISTRIBUTION_DOC_STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;
    @ApiModelProperty("单据状态含义")
    private String instructionDocStatusMeaning;
    @ApiModelProperty("需求时间")
    private String demandTime;
    @ApiModelProperty("备注")
    private String remark;
}
