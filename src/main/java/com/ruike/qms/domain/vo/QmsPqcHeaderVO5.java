package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * QmsPqcHeaderVO5
 *
 * @author: chaonan.hu@hand-china.com 2020/8/18 10:42:17
 **/
@Data
public class QmsPqcHeaderVO5 implements Serializable {
    private static final long serialVersionUID = 4520266241600275201L;

    @ApiModelProperty(value = "检验单行ID")
    private String pqcLineId;

    @ApiModelProperty(value = "序号")
    private String number;

    @ApiModelProperty(value = "检验项类别")
    private String inspectionType;

    @ApiModelProperty(value = "检验项目")
    private String inspection;

    @ApiModelProperty(value = "下限")
    private String standardFrom;

    @ApiModelProperty(value = "上限")
    private String standardTo;

    @ApiModelProperty(value = "单位")
    private String uomCode;

    @ApiModelProperty(value = "文本规格值")
    private String standardText;

    @ApiModelProperty(value = "检验工具")
    @LovValue(lovCode = "QMS.PQC_INSPECTION_TOOL", meaningField = "inspectionToolMeaning")
    private String inspectionTool;

    @ApiModelProperty(value = "检验工具含义")
    private String inspectionToolMeaning;

    @ApiModelProperty(value = "检验数")
    private Integer inspectionNum;

    @ApiModelProperty(value = "检验结果")
    @LovValue(lovCode = "QMS.INSPECTION_RESULT", meaningField = "inspectionResultMeaning")
    private String inspectionResult;

    @ApiModelProperty(value = "检验结果含义")
    private String inspectionResultMeaning;

    @ApiModelProperty(value = "规格类型")
    private String standardType;

    @ApiModelProperty(value = "附件ID")
    private String attachmentUuid;
}
