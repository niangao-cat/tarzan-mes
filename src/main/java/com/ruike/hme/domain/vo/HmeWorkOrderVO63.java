package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 工单管理
 *
 * @author chaonan.hu@hand-china.com 2020-07-09 19:33:23
 */
@Data
public class HmeWorkOrderVO63 implements Serializable {
    private static final long serialVersionUID = 6046097263040546862L;

    @ApiModelProperty(value = "选中的工单Id集合", required = true)
    private List<String> workOrderIdList;

}
