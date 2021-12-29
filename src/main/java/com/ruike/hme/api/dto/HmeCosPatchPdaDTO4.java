package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmeCosPatchPdaDTO4
 *
 * @author: chaonan.hu@hand-china.com 2020/8/26 10:24:35
 **/
@Data
public class HmeCosPatchPdaDTO4 implements Serializable {
    private static final long serialVersionUID = 2076875190804104891L;

    @ApiModelProperty(value = "工艺路线ID列表", required = true)
    private List<String> operationIdList;

    @ApiModelProperty(value = "盒数", required = true)
    private int boxTotal;

    @ApiModelProperty(value = "工单Id", required = true)
    private String workOrderId;

    @ApiModelProperty(value = "工位Id", required = true)
    private String workcellId;

    @ApiModelProperty(value = "站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "wafer", required = true)
    private String wafer;

    @ApiModelProperty(value = "设备ID")
    private String equipmentId;

    @ApiModelProperty(value = "班次日历ID", required = true)
    private String wkcShiftId;

    @ApiModelProperty(value = "容器类型ID", required = true)
    private String containerTypeId;

    @ApiModelProperty(value = "最大序号数")
    private Long maxNumber;

    @ApiModelProperty(value = "条码芯片数", required = true)
    private Long qty;

    @ApiModelProperty(value = "工段ID", required = true)
    private String wkcLineId;

    @ApiModelProperty(value = "产线ID", required = true)
    private String prodLineId;

    @ApiModelProperty(value = "批次号", required = true)
    private String lot;

    @ApiModelProperty(value = "设备编码(多个拼接)")
    private String assetEncoding;

    @ApiModelProperty(value = "金锡比")
    private String auSnRatio;

    @ApiModelProperty(value = "实验代码")
    private String labCode;

    @ApiModelProperty(value = "备注")
    private String labRemark;
}
