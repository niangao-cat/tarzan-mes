package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @Description 抽样方案类型LOV选择后带出信息
 * @Author tong.li
 * @Date 2020/5/14 16:03
 * @Version 1.0
 */
@Data
public class QmsIqcCheckPlatformCreateBringDTO2 implements Serializable {
    private static final long serialVersionUID = -2359109828287424523L;

    @ApiModelProperty(value = "AQL值")
    @LovValue(value = "QMS.IQC_AQL", meaningField = "aqlMeaning")
    private String aql;
    @ApiModelProperty(value = "AQL含义")
    private String aqlMeaning;

    @ApiModelProperty(value = "抽样数量")
    private String sampleSize;

    @ApiModelProperty(value = "检验水平")
    private String inspectionLevels;

    @ApiModelProperty(value = "AC")
    private String ac;
    @ApiModelProperty(value = "RE")
    private String re;

}
