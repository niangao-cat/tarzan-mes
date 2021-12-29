package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName WmsDistributionRevokeReturnDTO
 * @Description 配送单头信息
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/8 17:28
 * @Version 1.0
 **/
@Data
public class WmsDistributionRevokeReturnDTO implements Serializable {

    private static final long serialVersionUID = 5008589991431611770L;

    @ApiModelProperty("配送单Id")
    private String instructionDocId;

    @ApiModelProperty("配送单号")
    private String instructionDocNum;

    @ApiModelProperty("产线Id")
    private String prodLineId;

    @ApiModelProperty("产线")
    private String prodLineCode;

    @ApiModelProperty("产线")
    private String prodLineName;

    @ApiModelProperty("状态")
    @LovValue(value = "WMS.DISTRIBUTION_DOC_STATUS", meaningField = "statusMeaning")
    private String status;

    @ApiModelProperty("状态h含义")
    private String statusMeaning;

    @ApiModelProperty("工段Id")
    private String workcellId;

    @ApiModelProperty("工段")
    private String workcellCode;

    @ApiModelProperty("工段")
    private String workcellName;

    @ApiModelProperty("状态")
    private String remark;

    @ApiModelProperty("单据行")
    private List<WmsDistributionRevokeReturnDTO2> instructionList;
}
