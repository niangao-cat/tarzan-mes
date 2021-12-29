package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName HmeWoJobSnReturnDTO7
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/10/26 22:27
 * @Version 1.0
 **/
@Data
public class HmeWoJobSnReturnDTO7 implements Serializable {

    private static final long serialVersionUID = -5430321499213829974L;

    @ApiModelProperty(value = "来料录入Id")
    private String operationRecordId;

    @ApiModelProperty(value = "工单Id")
    private String workOrderId;

    @ApiModelProperty(value = "工单")
    private String workOrderNum;

    @ApiModelProperty(value = "wafer")
    private String wafer;

    @ApiModelProperty(value = "容器类型Id")
    private String containerTypeId;

    @ApiModelProperty(value = "容器类型编码")
    private String containerTypeCode;

    @ApiModelProperty(value = "容器类型描述")
    private String containerTypeDescription;

    @ApiModelProperty(value = "cos类型")
    private String cosType;

    @ApiModelProperty(value = "lotNo")
    private String lotNo;

    @ApiModelProperty(value = "AGV")
    private String averageWavelength;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "作业批次")
    private String jobBatch;

    @ApiModelProperty(value = "类型")
    private String barNum;

    @ApiModelProperty(value = "芯片数")
    private String cosNum;

    @ApiModelProperty(value = "工单来料芯片数")
    private String incomingQty;

    @ApiModelProperty("剩余数量")
    private String remainingQty;

    @ApiModelProperty("工艺id")
    private String operationId;

    @ApiModelProperty("站点Id")
    private String siteId;

    @ApiModelProperty("来源条码Id")
    private String materialLotId;

    @ApiModelProperty("来源条码Code")
    private String materialLotCode;

    @ApiModelProperty("数量")
    private Double qty;

    @ApiModelProperty(value = "转移目标")
    private List<HmeWoJobSnDTO7> targetList;

}
