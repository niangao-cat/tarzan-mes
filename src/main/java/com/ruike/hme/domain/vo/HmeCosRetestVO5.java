package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/20 14:06
 */
@Data
public class HmeCosRetestVO5 implements Serializable {

    private static final long serialVersionUID = 8779788831650623402L;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "WAFER")
    private String wafer;

    @ApiModelProperty(value = "容器类型")
    private String containerTypeCode;

    @ApiModelProperty(value = "COS类型")
    private String cosType;

    @ApiModelProperty(value = "Avg（nm）")
    private BigDecimal averageWavelength;

    @ApiModelProperty(value = "TYPE")
    private String type;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "录入批次")
    private String jobBatch;

    @ApiModelProperty(value = "LOTNO")
    private String lotNo;

    @ApiModelProperty(value = "来料条码")
    private String sourceLotCode;

    @ApiModelProperty(value = "条码数量")
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "剩余COS数量")
    private BigDecimal remainingQty;

    @ApiModelProperty(value = "工位")
    private String workcellId;

    @ApiModelProperty(value = "工艺")
    private String operationId;

    @ApiModelProperty(value = "班次")
    private String wkcShiftId;

    @ApiModelProperty(value = "工单物料")
    private String materialId;

    @ApiModelProperty(value = "拆分条码")
    private List<HmeCosRetestVO7> targetList;

    @ApiModelProperty(value = "金线供应商批次")
    private String goldSupplierLot;

    @ApiModelProperty(value = "热沉供应商批次")
    private String hotSinkSupplierLot;

    @ApiModelProperty(value = "热沉类型")
    private String hotSinkType;

    @ApiModelProperty(value = "站点")
    private String siteId;

    @ApiModelProperty(value = "产线")
    private String prodLineId;

    @ApiModelProperty(value = "批次")
    private String lot;
}
