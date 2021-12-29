package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * HmeProductionLineVO
 *
 * @author liyuan.lv@hand-china.com 2020/04/24 16:22
 */
@Data
public class HmeProductionLineVO implements Serializable {

    @ApiModelProperty(value = "生产线ID")
    private String prodLineId;
    @ApiModelProperty(value = "生产线编码")
    private String prodLineCode;
    @ApiModelProperty(value = "生产线名称")
    private String prodLineName;
}
