package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.*;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * description
 *
 * @author liyuan.lv@hand-china.com 2020/03/18 0:08
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoJobSnVO2 implements Serializable {
    private static final long serialVersionUID = 8159896234508816641L;

    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("工位编码")
    private String workcellCode;
    @ApiModelProperty("工单ID")
    private String workOrderId;
    @ApiModelProperty("工序作业ID")
    private String jobId;
    @ApiModelProperty("进站日期")
    private Date siteInDate;
    @ApiModelProperty("出站日期")
    private Date siteOutDate;
    @ApiModelProperty("班次ID")
    private String shiftId;
    @ApiModelProperty("班次编码")
    private String shiftCode;
    @ApiModelProperty("进站人")
    private Long siteInBy;
    @ApiModelProperty("SN料ID")
    private String snMaterialId;
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;
    @ApiModelProperty("EO")
    private String eoId;
    @ApiModelProperty("EO编码")
    private String eoNum;
    @ApiModelProperty("工艺路线ID")
    private String operationId;
    @ApiModelProperty("容器ID")
    private String containerId;
    @ApiModelProperty("工单数量")
    private Double woQuantity;
    @ApiModelProperty("作业类型")
    private String jobType;
    @ApiModelProperty("工艺步骤ID")
    private String eoStepId;
    @ApiModelProperty("工艺步骤加工次数")
    private Integer eoStepNum;
    @ApiModelProperty("返修标识")
    private String reworkFlag;
    @ApiModelProperty("工序作业容器ID")
    private String jobContainerId;
    @ApiModelProperty("来源容器ID")
    private String sourceContainerId;
    @ApiModelProperty("来源作业ID")
    private String sourceJobId;
    @ApiModelProperty("当前步骤序号")
    private Long currentStepSequence;
    @ApiModelProperty("当前步骤识别码")
    private String currentStepName;
    @ApiModelProperty("当前步骤识别码")
    private String currentStepDescription;
    @ApiModelProperty("下一步骤识别码")
    private String nextStepName;
    @ApiModelProperty("下一步骤识别码描述")
    private String nextStepDescription;
    @ApiModelProperty(value = "计划外作业ID")
    private String bydMaterialId;
    @ApiModelProperty("版本")
    private String productionVersion;
    @ApiModelProperty("物料类")
    private String itemType;
    @ApiModelProperty(value = "装配库位")
    private String locatorId;
    @ApiModelProperty(value = "库存批次")
    private String lotCode;
    @ApiModelProperty(value = "虚拟机标识")
    private String virtualFlag;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("预装数量")
    private BigDecimal prepareQty;

    @ApiModelProperty("是否批量作业平台SN条码扫描")
    private String batchProcessSnScanFlag;

    private List<HmeEoJobMaterialVO> materialVOList;
    private List<HmeEoJobLotMaterialVO> lotMaterialVOList;
    private List<HmeEoJobTimeMaterialVO> timeMaterialVOList;
    private List<HmeEoJobDataRecordVO> dataRecordVOList;
    @ApiModelProperty("条码类型")
    String codeType;

    @ApiModelProperty("实验代码")
    String labCode;
}
