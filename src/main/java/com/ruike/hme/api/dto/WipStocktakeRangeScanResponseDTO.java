package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 在制品盘点范围扫描 响应数据
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 13:59
 */
@Data
public class WipStocktakeRangeScanResponseDTO implements Serializable {
    private static final long serialVersionUID = 5507926047283225213L;

    @ApiModelProperty("范围对象类型")
    private String rangeObjectType;
    @ApiModelProperty("范围对象编码")
    private String rangeObjectCode;
    @ApiModelProperty("范围对象名称")
    private String rangeObjectName;
    @ApiModelProperty("范围对象Id")
    private String rangeObjectId;

    public WipStocktakeRangeScanResponseDTO() {
    }

    public WipStocktakeRangeScanResponseDTO(String rangeObjectType, String rangeObjectCode, String rangeObjectName, String rangeObjectId) {
        this.rangeObjectType = rangeObjectType;
        this.rangeObjectCode = rangeObjectCode;
        this.rangeObjectName = rangeObjectName;
        this.rangeObjectId = rangeObjectId;
    }
}
