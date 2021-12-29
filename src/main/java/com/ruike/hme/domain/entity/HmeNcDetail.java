package com.ruike.hme.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Xiong Yi 2020/07/07 20:35
 * @description:
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ExcelSheet(title = "工序不良报表")
public class HmeNcDetail implements Serializable {

    private static final long serialVersionUID = 7383406711599029902L;

    @ApiModelProperty("不良id")
    private String ncRecordId;

    @ApiModelProperty("rootId")
    private String workcellId;

    @ApiModelProperty("工位Id")
    private String stationId;

    @ApiModelProperty("生产线")
    @ExcelColumn(zh = "生产线", order = 1)
    private String description;

    @ApiModelProperty("生产线ID")
    private String productionLineId;

    @ApiModelProperty("工段")
    @ExcelColumn(zh = "工段", order = 2)
    private String workcellName;

    private String lineWorkcellId;

    @ApiModelProperty("工序")
    @ExcelColumn(zh = "工序", order = 3)
    private String process;

    private String processId;

    @ApiModelProperty("工位")
    @ExcelColumn(zh = "责任工位", order = 4)
    private String station;

    @ApiModelProperty("产品料号")
    @ExcelColumn(zh = "产品料号", order = 6)
    private String materialCode;

    @ApiModelProperty("产品描述")
    @ExcelColumn(zh = "产品描述", order = 7)
    private String materialName;

    @ApiModelProperty("工单号")
    @ExcelColumn(zh = "工单号", order = 8)
    private String workOrderNum;

    @ApiModelProperty("工单版本")
    private String productionVersion;

    @ApiModelProperty("单据状态")
    @LovValue(lovCode = "HME.NC_INCIDENT_STATUS", meaningField = "ncIncidentStatusMeaning")
    private String ncIncidentStatus;

    @ApiModelProperty("单据状态含义")
    private String ncIncidentStatusMeaning;

    @ApiModelProperty("质量状态")
    @LovValue(lovCode = "MT.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;

    @ApiModelProperty("质量状态含义")
    private String qualityStatusMeaning;

    @ApiModelProperty(value = "是否冻结")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "freezeFlagMeaning")
    private String freezeFlag;

    @ApiModelProperty(value = "是否冻结含义")
    private String freezeFlagMeaning;

    @ApiModelProperty(value = "是否转型")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "transformFlagMeaning")
    private String transformFlag;

    @ApiModelProperty(value = "是否转型含义")
    private String transformFlagMeaning;

    @ApiModelProperty("产品序列号")
    @ExcelColumn(zh = "产品序列号", order = 10)
    private String materialLotNum;

    @ApiModelProperty("发生时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    @ApiModelProperty("不良代码组")
    @ExcelColumn(zh = "不良代码组", order = 11)
    private String descriptionType;

    @ApiModelProperty("不良代码")
    @ExcelColumn(zh = "不良代码", order = 1)
    private String ncCode;

    @ApiModelProperty("不良代码描述")
    @ExcelColumn(zh = "不良代码描述", order = 12)
    private String ncDescription;

    @ApiModelProperty("申请人")
    @ExcelColumn(zh = "申请人", order = 16)
    private String realName;

    @ApiModelProperty("处理人")
    @ExcelColumn(zh = "处理人", order = 20)
    private String conductor;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("序列号id")
    private String materialId;

    @ApiModelProperty("产品id")
    private String materialLotId;

    @ApiModelProperty("处理人id")
    private String closedUserId;

    @ApiModelProperty("不良单号")
    @ExcelColumn(zh = "不良单号", order = 9)
    private String incidentNumber;

    @ApiModelProperty("提交人备注")
    @ExcelColumn(zh = "提交人备注", order = 17)
    private String comments;

    @ApiModelProperty("处理人备注")
    @ExcelColumn(zh = "处理人备注", order = 21)
    private String subComments;

    @ApiModelProperty("处置方式编码")
    private String processMethod;

    @ApiModelProperty("处置方式")
    @ExcelColumn(zh = "处置方式", order = 13)
    private String processMethodName;

    @ApiModelProperty(value = "提交时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dateTime;

    @ApiModelProperty(value = "提交时间")
    @ExcelColumn(zh = "提交时间", order = 18)
    private String dateTimeStr;

    @ApiModelProperty(value = "处理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date closedDateTime;

    @ApiModelProperty(value = "处理时间")
    @ExcelColumn(zh = "处理时间", order = 22)
    private String closedDateTimeStr;

    @ApiModelProperty(value = "转型物料编码")
    @ExcelColumn(zh = "转型物料编码", order = 14)
    private String transMaterialCode;

    @ApiModelProperty(value = "转型物料描述")
    @ExcelColumn(zh = "转型物料描述", order = 15)
    private String transMaterialDes;

    @ApiModelProperty(value = "转型物料Id")
    private String transMaterialId;

    @ApiModelProperty(value = "提交工位")
    private String rootCauseWorkcell;

    @ApiModelProperty(value = "提交工位名称")
    @ExcelColumn(zh = "提交工位名称", order = 5)
    private String rootCauseWorkcellName;
}