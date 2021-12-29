package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 工单管理
 *
 * @author chaonan.hu@hand-china.com 2020-07-09 16:12:08
 */
@Data
public class HmeWorkOrderVO61 implements Serializable {
    private static final long serialVersionUID = -6318034246992963561L;

    private String prodLineId;

    private String prodLineCode;

    private String prodLineName;

    @ApiModelProperty(value = "物料Id",required = true)
    private String materialId;
}
