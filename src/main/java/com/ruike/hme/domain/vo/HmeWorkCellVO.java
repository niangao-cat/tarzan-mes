package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeWorkCellVO - 工段LOV返回VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/10 10:01
 */
@Data
public class HmeWorkCellVO implements Serializable {

    private static final long serialVersionUID = -2741974874095218946L;

    @ApiModelProperty(value = "工段id")
    private String workcellId;

    @ApiModelProperty(value = "工段编码")
    private String workcellCode;

    @ApiModelProperty(value = "工段描述")
    private String workcellName;

}
