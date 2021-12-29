package com.ruike.qms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * QmsPqcHeaderVO4
 *
 * @author: chaonan.hu@hand-china.com 2020/8/18 10:34:23
 **/
@Data
public class QmsPqcHeaderVO4 implements Serializable {
    private static final long serialVersionUID = 8796947186976653165L;

    @ApiModelProperty(value = "检验单头ID")
    private String pqcHeaderId;

    @ApiModelProperty(value = "产线Id")
    private String prodLineId;

    @ApiModelProperty(value = "产线名称")
    private String prodLineName;

    @ApiModelProperty(value = "产品类型")
    private String materialType;

    @ApiModelProperty(value = "产品Id")
    private String materialId;

    @ApiModelProperty(value = "产品料号")
    private String materialCode;

    @ApiModelProperty(value = "产品描述")
    private String materialName;

    @ApiModelProperty(value = "序列号")
    private String materialLotCode;

    @ApiModelProperty(value = "工单Id")
    private String workOrderId;

    @ApiModelProperty(value = "工单")
    private String workOrderNum;

    @ApiModelProperty(value = "巡检单号")
    private String pqcNumber;

    @ApiModelProperty(value = "巡检时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionFinishDate;

    @ApiModelProperty(value = "检验人")
    private String qcBy;

    @ApiModelProperty(value = "检验人姓名")
    private String qcByName;

    @ApiModelProperty(value = "结果")
    @LovValue(value = "QMS.PQC_INSPECTION_RESULT",meaningField ="pqcInspectionResultMeaning")
    private String inspectionResult;

    @ApiModelProperty(value = "结果含义")
    private String inspectionResultMeaning;

    @ApiModelProperty(value = "班次")
    private String shiftCode;

    @ApiModelProperty(value = "备注")
    private String  remark;

    @ApiModelProperty(value = "状态")
    @LovValue(value = "QMS.PQC_INSPECTION_STATUS", meaningField = "inspectionStatusMeaning")
    private String inspectionStatus;

    @ApiModelProperty(value = "状态含义")
    private String inspectionStatusMeaning;
}
