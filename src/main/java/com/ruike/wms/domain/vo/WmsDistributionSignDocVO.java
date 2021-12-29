package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;

import java.util.Date;
import java.util.List;

/**
 * 配送签收单据头
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 19:31
 */
@Data
public class WmsDistributionSignDocVO {
    @ApiModelProperty("租户")
    private Long tenantId;
    @ApiModelProperty("配送单ID")
    private String instructionDocId;
    @ApiModelProperty("配送单号")
    private String instructionDocNum;
    @ApiModelProperty("产线编码")
    private String prodLineCode;
    @ApiModelProperty("工段编码")
    private String workcellCode;
    @ApiModelProperty("货位编码")
    private String locatorCode;
    @ApiModelProperty("需求时间")
    private Date demandTime;
    @ApiModelProperty("单据状态")
    @LovValue(lovCode = "WMS.DISTRIBUTION_DOC_STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;
    @ApiModelProperty("单据状态含义")
    private String instructionDocStatusMeaning;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("行列表")
    List<WmsDistributionSignLineVO> lineList;
}
