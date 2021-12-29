package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 工单管理
 *
 * @author chaonan.hu@hand-china.com 2020-07-09 17:27:13
 */
@Data
public class HmeWorkOrderVO62 implements Serializable {
    private static final long serialVersionUID = -8185038354524704265L;

    @ApiModelProperty(value = "选中的工单Id集合", required = true)
    private List<String> workOrderIdList;

    @ApiModelProperty(value = "选中的产线Id", required = true)
    private String prodLineId;
}
