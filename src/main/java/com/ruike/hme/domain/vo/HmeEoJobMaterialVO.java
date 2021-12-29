package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.ruike.hme.domain.entity.HmeEoJobBeyondMaterial;
import com.ruike.hme.infra.constant.HmeConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * MtEoStepJobTimeMaterialVO
 *
 * @author liyuan.lv@hand-china.com 2020/03/18 0:08
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoJobMaterialVO implements Serializable {

    private static final long serialVersionUID = 1309715438660139945L;

    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "工位ID")
    private String workcellId;
    @ApiModelProperty(value = "作业ID")
    private String jobId;
    @ApiModelProperty(value = "EO")
    private String eoId;
    @ApiModelProperty(value = "工艺ID")
    private String operationId;
    @ApiModelProperty(value = "工艺步骤ID")
    private String eoStepId;
    @ApiModelProperty(value = "作业平台编码")
    private String jobType;
    @ApiModelProperty(value = "SN编码")
    private String snNum;
    @ApiModelProperty("SN料ID")
    private String snMaterialId;
    @ApiModelProperty(value = "序列物料作业ID")
    private String jobMaterialId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料单位")
    private String primaryUomCode;
    @ApiModelProperty(value = "投料数量")
    private BigDecimal releaseQty;
    @ApiModelProperty(value = "计划外作业ID")
    private String bydMaterialId;
    @ApiModelProperty(value = "时效")
    private String availableTime;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "删除标识")
    private String deleteFlag;
    @ApiModelProperty(value = "是否投料")
    private Integer isReleased;
    @ApiModelProperty(value = "是否已投")
    private Integer isIssued;
    @ApiModelProperty(value = "是否工位查询")
    private String isWorkcellQuery;
    @ApiModelProperty("预装数量")
    private BigDecimal prepareQty;
    @ApiModelProperty("序列物料-是否替代物料")
    private String substituteFlag;
    @ApiModelProperty(value = "组件排序号")
    private Long lineNumber;
    @ApiModelProperty(value = "组件版本")
    private String bomComponentVersion;
    @ApiModelProperty("wkc组件匹配标识")
    private String wkcMatchedFlag;
    @ApiModelProperty("步骤")
    private String routerStepId;
    @ApiModelProperty("虚拟件标识")
    private String virtualFlag;
    @ApiModelProperty("库位ID")
    private String locatorId;
    @ApiModelProperty("批次")
    private String lotCode;
    @ApiModelProperty("版本")
    private String productionVersion;
    @ApiModelProperty("返修标识")
    private String reworkFlag;
    @ApiModelProperty("平台标识")
    private String pfType;
}
