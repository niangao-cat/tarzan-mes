package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * COS芯片录入
 *
 * @author yifan.xiong@hand-china.com 2020-8-31 10:35:34
 */
@Data
public class HmeCosChipInputVO implements Serializable {

    private static final long serialVersionUID = 1167160138101898928L;

    @ApiModelProperty("条码ID")
    private String materialLotId;

    @ApiModelProperty("JobId")
    private String jobId;

    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("工单芯片数")
    private String cosNum;

    @ApiModelProperty("盒子编号")
    private String materialLotCode;

    @ApiModelProperty("COS数量")
    private String primaryUomQty;

    @ApiModelProperty("WAFER")
    private String wafer;

    @ApiModelProperty("来料批次")
    private String attrValue;

    @ApiModelProperty("芯片总数")
    private String qty;

    @ApiModelProperty("产品编码")
    private String materialCode;

    @ApiModelProperty("产品名称")
    private String materialName;

    @ApiModelProperty("备注")
    private String remark;
}
