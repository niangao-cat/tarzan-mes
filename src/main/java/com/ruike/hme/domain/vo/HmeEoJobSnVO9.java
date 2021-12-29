package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoJobSnVO9 implements Serializable {
    private static final long serialVersionUID = -5925648770479271444L;
    @ApiModelProperty("主键ID")
    private String jobMaterialId;
    @ApiModelProperty("生产指令ID")
    private String workOrderId;
    @ApiModelProperty("生产指令")
    private String workOrderNum;
    @ApiModelProperty("执行指令ID")
    private String eoId;
    @ApiModelProperty("执行指令")
    private String eoNum;
    @ApiModelProperty("投料物料ID")
    private String materialId;
    @ApiModelProperty("投料物料编码")
    private String materialCode;
    @ApiModelProperty("投料物料描述")
    private String materialName;
    @ApiModelProperty("物料版本")
    private String productionVersion;
    @ApiModelProperty("条码ID")
    private String materialLotId;
    @ApiModelProperty("条码")
    private String materialLotCode;
    @ApiModelProperty("投料数量")
    private BigDecimal releaseQty;
    @ApiModelProperty("批次")
    private String lotCode;
    @ApiModelProperty("投料时间")
    private Date creationDate;
    @ApiModelProperty("投料人ID")
    private Long createdBy;
    @ApiModelProperty("投料人")
    private String loginName;
    @ApiModelProperty("工序作业ID")
    private String jobId;
    @ApiModelProperty("当前工序作业ID-返修平台使用")
    private String currentJobId;
    @ApiModelProperty("工序作业类型")
    private String jobType;
    @ApiModelProperty("库位ID")
    private String locatorId;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("工艺步骤ID")
    private String eoStepId;
    @ApiModelProperty("班次ID")
    private String shiftId;
    @ApiModelProperty("虚拟件标识")
    private String virtualFlag;
    @ApiModelProperty("站点")
    private String siteId;

    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("条码ID")
    private String sourceMaterialLotId;
    @ApiModelProperty("条码CODE")
    private String sourceMaterialLotCode;
    @ApiModelProperty("物料类型")
    private String materialType;

    @ApiModelProperty("退料条码")
    private String backMaterialLotCode;
    @ApiModelProperty("退料数量")
    private BigDecimal backQty;
    @ApiModelProperty("物料升级标识")
    private String upgradeFlag;
    @ApiModelProperty("执行作业标识说明")
    private String identification;
    @ApiModelProperty("物料单位ID")
    private String primaryUomId;
    @ApiModelProperty("物料单位")
    private String UomCode;
    @ApiModelProperty(value = "班次日期")
    private Date shiftDate;
    @ApiModelProperty(value = "用于标识该日期下班次")
    private String shiftCode;
    @ApiModelProperty(value = "工单产线编码")
    private String prodLineCode;
    @ApiModelProperty(value = "工单产线有效性")
    private String prodLineEnableFlag;
    @ApiModelProperty(value = "指定工艺路线返修标识")
    private String designedReworkFlag;
    @ApiModelProperty(value = "是否报废")
    private Integer isScrap;
    @ApiModelProperty(value = "返修来源标识")
    private String reworkSourceFlag;

    @ApiModelProperty("当前进站生产指令")
    private String currentWorkOrderNum;
    @ApiModelProperty("当前工位Id")
    private String currentWorkcellId;

    @ApiModelProperty("退料查询工艺ID")
    private String backOperationId;
    @ApiModelProperty("进站工艺步骤ID")
    private String siteInEoStepId;
    @ApiModelProperty("进站EOId")
    private String siteInEoId;
    @ApiModelProperty("装配数量")
    private BigDecimal assembleQty;
    @ApiModelProperty(value = "首序作业平台标识")
    String isFirstProcess;

    @ApiModelProperty(value = "泵浦源作业平台标识")
    private String isPumpProcess;
}
