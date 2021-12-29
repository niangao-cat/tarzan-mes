package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnBatchVO implements Serializable {

    private static final long serialVersionUID = -8869146263745463212L;
    @ApiModelProperty("是否容器进站")
    Boolean isContainer;
    @ApiModelProperty("进站SN列表")
    List<HmeEoJobSnVO3> snLineList;
    @ApiModelProperty("SN物料信息列表")
    List<HmeEoJobSnVO5> snVOList;
    @ApiModelProperty("EO当前步骤")
    Map<String, List<HmeRouterStepVO3>> currentStepMap;
    @ApiModelProperty("EO最近步骤")
    Map<String, HmeRouterStepVO> nearStepMap;
    @ApiModelProperty("条码类型")
    String codeType;
}
