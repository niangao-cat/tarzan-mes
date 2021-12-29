package com.ruike.reports.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * 材料不良明细报表
 *
 * @author wenqiang.yin@hand-china.com 2021/02/02 9:42
 */
@Data
public class HmeMaterielBadDetailedVO implements Serializable {

    private static final long serialVersionUID = -9085250948431062823L;

    @ApiModelProperty("产线编码")
    private String prodLineCode;
    @ApiModelProperty("产线描述")
    private String prodLineName;
    @ApiModelProperty("工段编码")
    private String workcellCode;
    @ApiModelProperty("工段描述")
    private String workcellName;
    @ApiModelProperty("工序编码")
    private String procedureCode;
    @ApiModelProperty("工序描述")
    private String procedureName;
    @ApiModelProperty("提交工位编码")
    private String stationCode;
    @ApiModelProperty("提交工位描述")
    private String stationName;
    @ApiModelProperty("产品料号")
    private String materialCode;
    @ApiModelProperty("产品描述")
    private String materialName;
    @ApiModelProperty("工单号")
    private String workOrderNum;
    @ApiModelProperty("工单版本")
    private String productionVersion;
    @ApiModelProperty("不良单号")
    private String incidentNumber;
    @ApiModelProperty("单据状态")
    @LovValue(value = "HME.NC_INCIDENT_STATUS", meaningField = "ncIncidentStatusMeaning")
    private String ncIncidentStatus;
    @ApiModelProperty("单据状态含义")
    private String ncIncidentStatusMeaning;
    @ApiModelProperty("条码号")
    private String materialLotCode;
    @ApiModelProperty("组件料号")
    private String assemblyCode;
    @ApiModelProperty("单位")
    private String uomCode;
    @ApiModelProperty("组件物料描述")
    private String assemblyName;
    @ApiModelProperty("该条码已投数量")
    private String releasQty;
    @ApiModelProperty("不良申请数量")
    private String qty;
    @ApiModelProperty("组件条码批次")
    private String lot;
    @ApiModelProperty("供应商批次")
    private String attrValue;
    @ApiModelProperty("是否冻结")
    private String freezeFlag;
    @ApiModelProperty("不良代码组编码")
    private String ncGroupCode;
    @ApiModelProperty("不良代码组描述")
    private String description;
    @ApiModelProperty("不良代码编码")
    private String ncCode;
    @ApiModelProperty("不良代码描述")
    private String ncDescription;
    @ApiModelProperty("处置方式")
    @LovValue(value = "HME.NC_PROCESS_METHOD", meaningField = "processMethodMeaning")
    private String processMethod;
    @ApiModelProperty("处置方式含义")
    private String processMethodMeaning;
    @ApiModelProperty("责任工位编码")
    private String dutyCode;
    @ApiModelProperty("责任工位描述")
    private String dutyName;
    @ApiModelProperty("提交人")
    private String realName;
    @ApiModelProperty("提交时间")
    private String dateTime;
    @ApiModelProperty("提交人备注")
    private String comments;
    @ApiModelProperty("处理人")
    private String closedName;
    @ApiModelProperty("处理时间")
    private String closedDateTime;
    @ApiModelProperty("处理人备注")
    private String closedComments;

}
