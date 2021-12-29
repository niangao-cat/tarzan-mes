package com.ruike.hme.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 投料记录参数
 *
 * @author jiangling.zheng@hand-china.com 2020-10-27 20:24
 */
@Data
public class HmeWoInputRecordDTO5 implements Serializable {

    private static final long serialVersionUID = -5035538972425603059L;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "组件ID")
    private String bomComponentId;
    @ApiModelProperty(value = "工步ID")
    private String routerStepId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料ID")
    private String planFlag;
    @ApiModelProperty(value = "退料数量")
    private BigDecimal outQty;
    @ApiModelProperty(value = "本次投料信息")
    List<HmeWoInputRecordDTO2> dtoList;


}
