package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 投料记录参数
 *
 * @author jiangling.zheng@hand-china.com 2020-10-27 20:24
 */
@Data
public class HmeWoInputRecordDTO3 implements Serializable {

    private static final long serialVersionUID = -364763255736578983L;

    @ApiModelProperty(value = "工单ID")
    @NotBlank
    private String workOrderId;
    @ApiModelProperty(value = "工步ID")
    private String routerStepId;
    @ApiModelProperty(value = "物料ID")
    @NotBlank
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "特殊库存标识")
    private String specialInvFlag;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
    @ApiModelProperty(value = "条码")
    @NotBlank
    private String materialLotCode;

}
