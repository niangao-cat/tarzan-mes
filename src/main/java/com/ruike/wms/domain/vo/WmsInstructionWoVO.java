package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 单据和工单关系
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 20:31
 */
@Data
public class WmsInstructionWoVO {
    @ApiModelProperty("工单号")
    private String workOrderNum;
    @ApiModelProperty("单据行ID")
    private String instructionId;
}
