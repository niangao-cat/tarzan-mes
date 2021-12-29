package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * HmeCosYieldComputeVO2
 *
 * @author: chaonan.hu@hand-china.com 2021/09/17 15:23
 **/
@Data
public class HmeCosYieldComputeVO2 implements Serializable {
    private static final long serialVersionUID = 5037713255472511101L;

    @ApiModelProperty(value = "记录的是每个COS类型与WAFER组合下的物料批集合")
    private Map<String, List<String>> cosTypeWaferMaterialLotIdMap;

    @ApiModelProperty(value = "记录的是每个COS类型与WAFER组合下是否有在制品")
    private Map<String, String> cosTypeWaferMfFlagMap;

    @ApiModelProperty(value = "记录的是每个WAFER下是否有在制品")
    private Map<String, String> waferMfFlagMap;

    @ApiModelProperty(value = "每个COS类型与WAFER组合下的所有物料批")
    private List<String> materialLotIdList;

}
