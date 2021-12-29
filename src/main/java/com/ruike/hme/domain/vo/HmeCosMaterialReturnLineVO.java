package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName HmeCOSMaterialReturnLineVO
 * @Description COS退料-工单组件
 * @Author lkj
 * @Date 2020/12/11
 */
@Data
public class HmeCosMaterialReturnLineVO {

    @ApiModelProperty(value = "BOM组件ID")
    private String bomComponentId;

    @ApiModelProperty(value = "BOM组件ID")
    private String lineNumber;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "工单数量单位ID")
    private String uomId;

    @ApiModelProperty(value = "工单数量单位编码")
    private String uomCode;

    @ApiModelProperty(value = "工单数量单位名称")
    private String uomName;

    @ApiModelProperty(value = "单位用量")
    private BigDecimal qty;

    @ApiModelProperty(value = "需求数量")
    private BigDecimal demandQty;

    @ApiModelProperty(value = "装配数量")
    private BigDecimal assembleQty;

    @ApiModelProperty(value = "工单完成数量")
    private BigDecimal completedQty;

    @ApiModelProperty(value = "可退料数量")
    private BigDecimal returnQty;

    @ApiModelProperty(value = "物料组")
    private String itemGroup;
}
