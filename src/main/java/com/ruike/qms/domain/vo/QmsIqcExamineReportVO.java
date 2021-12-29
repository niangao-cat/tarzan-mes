package com.ruike.qms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * QmsIqcExamineReportVO
 *
 * @author: chaonan.hu@hand-china.com 2020/12/10 10:45:23
 **/
@Data
public class QmsIqcExamineReportVO implements Serializable {
    private static final long serialVersionUID = -7082392352615376108L;

    @ApiModelProperty(value = "单据ID")
    private String iqcHeaderId;

    @ApiModelProperty(value = "单据号")
    private String iqcNumber;

    @ApiModelProperty(value = "供应商ID")
    private String supplierId;

    @ApiModelProperty(value = "供应商编码")
    private String supplierName;

    @ApiModelProperty(value = "检验组ID")
    private String tagGroupId;

    @ApiModelProperty(value = "检验组编码")
    private String tagGroupCode;

    @ApiModelProperty(value = "检验结果")
    @LovValue(value = "QMS.INSPECTION_STATUS", meaningField = "inspectionResultMeaning")
    private String inspectionResult;

    @ApiModelProperty(value = "检验结果含义")
    private String inspectionResultMeaning;

    @ApiModelProperty(value = "检验员ID")
    private Long lastUpdatedBy;

    @ApiModelProperty(value = "检验员")
    private String lastUpdatedByName;

    @ApiModelProperty(value = "不合格原因")
    private String remark;

    @ApiModelProperty(value = "检验完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionFinishDate;
}
