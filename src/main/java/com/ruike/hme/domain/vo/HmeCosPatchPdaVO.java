package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmeCosPatchPdaVO
 *
 * @author: chaonan.hu@hand-china.com 2020/8/24 16:44:28
 **/
@Data
public class HmeCosPatchPdaVO implements Serializable {
    private static final long serialVersionUID = -2021551787053963217L;

    @ApiModelProperty(value = "条码数据")
    List<HmeCosPatchPdaVO5> materialLotList;

    private String woJobSnId;

    private String operationRecordId;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工单编码")
    private String workOrderNum;

    @ApiModelProperty(value = "工单数量")
    private BigDecimal workOrderQty;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "投料数量")
    private BigDecimal assembleQty;

    @ApiModelProperty(value = "cos类型")
    private String cosType;

    @ApiModelProperty(value = "剩余芯片数")
    private Long surplusCosNum;

    @ApiModelProperty(value = "wafer")
    private String wafer;

    @ApiModelProperty(value = "可新增数量")
    private BigDecimal addQty;

    @ApiModelProperty(value = "完工数量")
    private BigDecimal completedQty;

    @ApiModelProperty(value = "工单贴片完成数")
    private BigDecimal achieveQty;

}
