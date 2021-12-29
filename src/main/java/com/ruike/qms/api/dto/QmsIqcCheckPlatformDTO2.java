package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 抽样方案类型LOV
 * @Author tong.li
 * @Date 2020/5/21 20:06
 * @Version 1.0
 */
@Data
public class QmsIqcCheckPlatformDTO2 implements Serializable {
    private static final long serialVersionUID = 4645827478533660514L;

    @ApiModelProperty(value = "抽样方案ID")
    private String sampleTypeId;
    @ApiModelProperty(value = "抽样方案编码")
    private String sampleTypeCode;
    @ApiModelProperty(value = "抽样方案名称")
    private String sampleTypeName;
}
