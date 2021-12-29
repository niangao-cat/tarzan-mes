package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 产品追溯
 *
 * @author jiangling.zheng@hand-china.com 2020-04-21 13:18
 */
@Data
public class HmeEoTraceBackQueryDTO5 implements Serializable {

    private static final long serialVersionUID = -2950697706441942843L;

    private List<HmeEoTraceBackQueryDTO2> materialList;
    private List<HmeEoTraceBackQueryDTO3> jobDataList;
}
