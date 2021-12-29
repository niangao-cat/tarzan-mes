package com.ruike.qms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * QmsPqcHeaderVO3
 *
 * @author: chaonan.hu@hand-china.com 2020/8/17 21:40:46
 **/
@Data
public class QmsPqcHeaderVO3 implements Serializable {
    private static final long serialVersionUID = -5319130721663774007L;

    @ApiModelProperty(value = "巡检单头Id")
    private String pqcHeaderId;

    @ApiModelProperty(value = "序号")
    private Integer number;

    @ApiModelProperty(value = "巡检单号")
    private String pqcNumber;

    @ApiModelProperty(value = "产品类型")
    private String materialType;

    @ApiModelProperty(value = "产品Id")
    private String materialId;

    @ApiModelProperty(value = "产品料号")
    private String materialCode;

    @ApiModelProperty(value = "产品描述")
    private String materialName;

    @ApiModelProperty(value = "工单Id")
    private String workOrderId;

    @ApiModelProperty(value = "工单")
    private String workOrderNum;

    @ApiModelProperty(value = "巡检时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionFinishDate;

    @ApiModelProperty(value = "结果")
    @LovValue(value = "QMS.PQC_INSPECTION_RESULT", meaningField = "inspectionResultMeaning")
    private String inspectionResult;

    @ApiModelProperty(value = "结果含义")
    private String inspectionResultMeaning;

    @ApiModelProperty(value = "状态")
    @LovValue(value = "QMS.PQC_INSPECTION_STATUS", meaningField = "inspectionStatusMeaning")
    private String inspectionStatus;

    @ApiModelProperty(value = "状态含义")
    private String inspectionStatusMeaning;
}
