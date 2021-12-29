package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeContainerCapacity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author sanfeng.zhang@hand-china.com 2020/8/10 15:28
 */
@Data
public class HmeContainerCapacityVO extends HmeContainerCapacity {

    @ApiModelProperty(value = "容器类型")
    private String containerTypeName;

    @ApiModelProperty(value = "工艺编码")
    private String operationName;

    @ApiModelProperty(value = "工艺名称")
    private String operationDesc;

    @ApiModelProperty(value = "列数")
    private Double length;

    @ApiModelProperty(value = "行数")
    private Double width;

    @ApiModelProperty(value = "COS类型名称")
    private String cosTypeName;

    @ApiModelProperty(value = "规则名称")
    private String rulesName;
}
