package com.ruike.qms.domain.vo;

import com.ruike.qms.domain.entity.QmsIqcLine;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 查询iqc行信息
 * @author: han.zhang
 * @create: 2020/05/19 18:47
 */
@Getter
@Setter
@ToString
public class QmsIqcAuditQueryLineVO extends QmsIqcLine implements Serializable {
    private static final long serialVersionUID = -7808607768221128190L;

    @ApiModelProperty(value = "抽样方案类型编码")
    private String sampleTypeCode;
    @ApiModelProperty(value = "AC和RE值")
    private String acAndRe;
    @ApiModelProperty(value = "规格范围")
    private String standardRange;
    @ApiModelProperty(value = "规格单位编码")
    private String standardUomCode;
    @ApiModelProperty(value = "检验类型含义")
    private String inspectionTypeMeaning;
    @ApiModelProperty(value = "检验水平含义")
    private String defectLevelsMeaning;
    @ApiModelProperty(value = "检验结果含义")
    private String inspectionResultMeaning;
}