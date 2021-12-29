package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 来料转移-扫描条码VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/12/28 19:47
 */
@Data
public class HmeCosOperationTransferVO implements Serializable {

    private static final long serialVersionUID = -4241064497685109563L;

    @ApiModelProperty(value = "条码id")
    private String materialLotId;

    @ApiModelProperty(value = "条码")
    private String materialLotCode;

    @ApiModelProperty(value = "条码数量")
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "芯片类型")
    @LovValue(lovCode = "HME_COS_TYPE", meaningField = "cosTypeMeaning")
    private String cosType;

    @ApiModelProperty(value = "芯片类型含义")
    private String cosTypeMeaning;

    @ApiModelProperty(value = "WAFER")
    private String waferNum;

    @ApiModelProperty(value = "录入批次")
    private String workingLot;

    @ApiModelProperty(value = "TYPE")
    private String type;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "工单Id")
    private String workOrderId;

    @ApiModelProperty(value = "LOTNO")
    private String lotNo;

    @ApiModelProperty(value = "平均波长")
    private String avgWaveLength;

    @ApiModelProperty(value = "当前工艺步骤")
    private String currentRouterStep;

    @ApiModelProperty(value = "条码列表")
    private List<HmeCosOperationTransferVO2> materialLotList;
}
