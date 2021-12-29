package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.order.domain.entity.MtWorkOrder;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/9/24 10:26
 */
@Data
public class HmeWoReleaseVO implements Serializable {

    private static final long serialVersionUID = 133960294281132786L;

    @ApiModelProperty(value = "工单")
    private MtWorkOrder mtWorkOrder;

    @ApiModelProperty(value = "eoId")
    private List<String> eoIdList;
}
