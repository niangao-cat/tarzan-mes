package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 售后拆机bom行信息
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 15:05
 */
@Data
public class HmeServiceSplitBomLineVO {
    @ApiModelProperty(value = "bom组件ID")
    private String bomComponentId;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "生产指令ID")
    private String materialName;

    @ApiModelProperty(value = "bom用量")
    private String usageQty;

    @ApiModelProperty(value = "单位Id")
    private String uomId;

    @ApiModelProperty(value = "单位名称")
    private String uomName;

    @ApiModelProperty(value = "物料组")
    private String itemGroup;

    @ApiModelProperty(value = "物料组描述")
    private String itemGroupDescription;

}
