package com.ruike.qms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;

/**
 * QmsPqcHeaderQueryVO
 * @description: 巡检单查询头返回
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/05 10:54
 */
@Data
@ExcelSheet(zh = "巡检单")
public class QmsPqcHeaderQueryVO implements Serializable {

    private static final long serialVersionUID = 6797588129241130011L;

    @ApiModelProperty(value = "工单Id")
    private String woId;

    @ApiModelProperty(value = "检验单头表主键")
    private String pqcHeaderId;

    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;

    @ApiModelProperty(value = "检验单号")
    @ExcelColumn(zh = "巡检单",order = 3)
    private String pqcNumber;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "单据状态")
    @LovValue(value = "QMS.PQC_00002", meaningField = "inspectionStatusMeaning")
    private String inspectionStatus;

    @ApiModelProperty(value = "状态含义")
    @ExcelColumn(zh = "单据状态",order = 4)
    private String inspectionStatusMeaning;

    @ApiModelProperty(value = "产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @ApiModelProperty(value = "创建时间")
    @ExcelColumn(zh = "创建时间",order = 11)
    private String creationDateStr;

    @ApiModelProperty(value = " 检验结果")
    @LovValue(value = "QMS.INSPECTION_STATUS", meaningField = "inspectionResultMeaning")
    private String inspectionResult;

    @ApiModelProperty(value = " 检验结果")
    @ExcelColumn(zh = "检验结果",order = 5)
    private String inspectionResultMeaning;

    @ApiModelProperty(value = " 备注")
    @ExcelColumn(zh = "备注",order = 9)
    private String remark;

    @ApiModelProperty(value = " 检验员")
    @ExcelColumn(zh = "检验员",order = 10)
    private String lastUpdatedByName;

    @ApiModelProperty(value = "工单")
    @ExcelColumn(zh = "工单",order = 7)
    private String workOrderNum;

    @ApiModelProperty(value = " 产品SN")
    @ExcelColumn(zh = "SN",order = 6)
    private String materialLotCode;

    @ApiModelProperty(value = " 物料")
    @ExcelColumn(zh = "产品",order = 7)
    private String materialName;

    @ApiModelProperty(value = " 生产线")
    @ExcelColumn(zh = "生产线",order = 2)
    private String prodLineName;

    @ApiModelProperty(value = " 车间")
    @ExcelColumn(zh = "车间",order = 1)
    private String workshopName;

    @ApiModelProperty(value = " 事业部")
    @ExcelColumn(zh = "事业部",order = 0)
    private String departmentName;

    @ApiModelProperty(value = " 工序")
    @ExcelColumn(zh = "工序",order = 8)
    private String processName;

}
